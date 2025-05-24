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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author unknowIfGuestInDream
 */
public class SqliteAccountTest {
    /**
     * 以嵌入式(本地)连接方式连接数据库
     */
    private static final String DRIVER_CLASS = "org.sqlite.JDBC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static String dbParentPath;
    private static String TEST_DB_URL;

    @BeforeAll
    static void init() {
        dbParentPath = new File(ResourceUtil.getResource("database/init.sql").getPath()).getParent();
        TEST_DB_URL = "jdbc:sqlite:" + Paths.get(dbParentPath).resolve("sqliteDB.db");
        System.out.println(TEST_DB_URL);
    }

    @Test
    void testInsertAndGetUsers() throws ClassNotFoundException, SQLException {
        //与数据库建立连接
        Class.forName(DRIVER_CLASS);
        Connection conn = DriverManager.getConnection(TEST_DB_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();

        //删除表
        statement.execute("DROP TABLE IF EXISTS USER_INF");
        //创建表
        statement.execute(
            "CREATE TABLE USER_INF(id VARCHAR(50) PRIMARY KEY, name VARCHAR(50) NOT NULL, sex VARCHAR(50) NOT NULL)");

        //插入数据
        statement.executeUpdate("INSERT INTO USER_INF VALUES('1', '程咬金', '男') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES('2', '孙尚香', '女') ");
        statement.executeUpdate("INSERT INTO USER_INF VALUES('3', '猴子', '男') ");

        //查询数据
        ResultSet resultSet = statement.executeQuery("select * from USER_INF");
        while (resultSet.next()) {
            System.out.println(
                resultSet.getInt("id") + ", " + resultSet.getString("name") + ", " + resultSet.getString("sex"));
        }
        //关闭连接
        statement.close();
        conn.close();
    }
}
