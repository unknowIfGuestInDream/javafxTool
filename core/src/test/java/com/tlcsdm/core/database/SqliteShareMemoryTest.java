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

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 共享内存数据库.
 * 默认情况下，每个 :memory: 连接都会创建独立的内存数据库。要共享内存数据库：使用命名内存数据库
 *
 * @author unknowIfGuestInDream
 */
public class SqliteShareMemoryTest {
    private static final String SHARED_URL =
        "jdbc:sqlite:file:sharedmem?mode=memory&cache=shared";

    @Test
    void testProductCount() {
        // 线程1创建表并插入数据
        new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(SHARED_URL);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("CREATE TABLE IF NOT EXISTS messages (id INTEGER, content TEXT)");
                stmt.execute("INSERT INTO messages VALUES(1, 'Hello from Thread 1')");
                Thread.sleep(1000); // 等待线程2读取
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 线程2读取数据
        new Thread(() -> {

            try (Connection conn = DriverManager.getConnection(SHARED_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM messages")) {

                Thread.sleep(2000); // 等待线程1写入
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + ": " + rs.getString("content"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
