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
import com.tlcsdm.core.database.sqlite.UserDao;

import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
public class Sqlite1Test {

    private static String dbParentPath;
    private static String TEST_DB_URL;
    private UserDao userDao;

    @BeforeAll
    static void init() {
        dbParentPath = new File(ResourceUtil.getResource("database/init.sql").getPath()).getParent();
        TEST_DB_URL = "jdbc:sqlite:" + Paths.get(dbParentPath).resolve("usersqlite.db");
        System.out.println(TEST_DB_URL);
    }

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new UserDao(TEST_DB_URL);
        userDao.deleteAllUsers();
    }

    @Test
    void testInsertAndGetUsers() throws SQLException {
        // 插入测试数据
        userDao.insertUser("Alice", "alice@example.com");
        userDao.insertUser("Bob", "bob@example.com");

        // 验证数据
        List<String> names = userDao.getAllUserNames();
        Assertions.assertEquals(2, names.size());
        Assertions.assertTrue(names.contains("Alice"));
        Assertions.assertTrue(names.contains("Bob"));
    }

    @Test
    void testEmptyDatabase() throws SQLException {
        List<String> names = userDao.getAllUserNames();
        Assertions.assertTrue(names.isEmpty());
    }

    @Test
    void testDuplicateEmail() {
        // 测试唯一约束
        Assertions.assertDoesNotThrow(() -> userDao.insertUser("Alice", "alice@example.com"));
        SQLException exception = Assertions.assertThrows(SQLException.class,
            () -> userDao.insertUser("Alice", "alice@example.com"));
        Assertions.assertTrue(exception.getMessage().contains("UNIQUE constraint failed"));
    }
}
