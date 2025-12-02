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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 内存数据库.
 *
 * @author unknowIfGuestInDream
 */
public class SqliteMemoryTest {
    private static Connection conn;

    @BeforeAll
    static void setup() throws SQLException {
        // 创建内存数据库连接
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");

        // 初始化数据库结构
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "price REAL)");
        }
    }

    @BeforeEach
    void prepareData() throws SQLException {
        // 每个测试前清空并插入测试数据
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM products");
            stmt.execute("INSERT INTO products(name, price) VALUES('Laptop', 999.99)");
            stmt.execute("INSERT INTO products(name, price) VALUES('Phone', 699.99)");
        }
    }

    @Test
    void testProductCount() throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
            rs.next();
            Assertions.assertEquals(2, rs.getInt(1));
        }
    }

    @Test
    void testProductInsert() throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
            "INSERT INTO products(name, price) VALUES(?, ?)")) {
            pstmt.setString(1, "Tablet");
            pstmt.setDouble(2, 499.99);
            Assertions.assertEquals(1, pstmt.executeUpdate());
        }

        // 验证插入
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
            rs.next();
            Assertions.assertEquals(3, rs.getInt(1));
        }
    }

    @AfterAll
    static void cleanup() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
