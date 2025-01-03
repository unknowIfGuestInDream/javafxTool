/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

import cn.hutool.core.thread.ThreadUtil;
import com.tlcsdm.core.javafx.util.ConfigureUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * H2 数据库.
 *
 * @author unknowIfGuestInDream
 */
class H2Test {

    private static DataSourceUtil dataSourceUtil = null;

    @BeforeAll
    static void init() {
        String dbPath = ConfigureUtil.getConfigureH2Path() + "/test";
        System.out.println("jdbc:h2:" + dbPath);
        dataSourceUtil = DataSourceUtilFactory.getDataSourceUtil("jdbc:h2:" + dbPath, "org.h2.Driver", "sa", "sa");
    }

    @Test
    @Order(1)
    void crateTable() throws SQLException {
        dataSourceUtil.execute("DROP TABLE IF EXISTS USER_INF;");
        dataSourceUtil.execute(
            "CREATE TABLE USER_INF(id VARCHAR(50) PRIMARY KEY, name VARCHAR(50) NOT NULL, sex VARCHAR(50) NOT NULL);");
        ThreadUtil.safeSleep(1000);
    }

    @Test
    @Order(2)
    void addInfo() throws SQLException {
        dataSourceUtil.executeUpdate("INSERT INTO USER_INF VALUES('1', '程咬金', '男');");
        dataSourceUtil.executeUpdate("INSERT INTO USER_INF VALUES('2', '孙尚香', '女');");
        dataSourceUtil.executeUpdate("INSERT INTO USER_INF VALUES('3', '猴子', '男');");
    }

    @Test
    @Order(3)
    void isInfoExits() {
        List<Map<String, Object>> list = dataSourceUtil.executeQuery("select * from USER_INF;");
        System.out.println(list.size());
    }

    @Test
    @Order(4)
    void releaseConnection() {
        dataSourceUtil.close();
    }
}
