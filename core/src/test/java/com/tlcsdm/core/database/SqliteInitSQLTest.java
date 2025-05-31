/*
 * Copyright (c) 2025 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.core.database;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 初始化脚本.
 *
 * @author unknowIfGuestInDream
 */
public class SqliteInitSQLTest {

    private static Connection conn;

    @BeforeAll
    static void setup() throws Exception {
        // 创建内存数据库连接
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        // 执行初始化脚本
        initializeInMemoryDatabase(conn);
        // 禁用自动提交以确保连接保持活跃
        conn.setAutoCommit(false);
    }

    @Test
    void testUserCount() throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
            rs.next();
            Assertions.assertEquals(3, rs.getInt(1));
        }
    }

    @AfterAll
    static void cleanup() throws SQLException {
        if (conn != null) {
            try {
                conn.rollback(); // 回滚任何未提交的更改
            } finally {
                conn.close();
            }
        }
    }

    private static void initializeInMemoryDatabase(Connection conn) throws SQLException, IOException {
        try (InputStream is = ResourceUtil.getResource("database/init.sql").openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             Statement stmt = conn.createStatement()) {

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                // 跳过注释和空行
                if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                    sb.append(line);
                    // 检查是否以分号结束（完整SQL语句）
                    if (line.trim().endsWith(";")) {
                        String sql = sb.toString();
                        sb = new StringBuilder();
                        stmt.execute(sql);
                    }
                }
            }
        }
    }
}
