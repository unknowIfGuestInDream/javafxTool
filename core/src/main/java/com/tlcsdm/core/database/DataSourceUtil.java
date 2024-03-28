/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

import cn.hutool.log.StaticLog;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 数据源工具抽象类.
 *
 * @author unknowIfGuestInDream
 */
public abstract class DataSourceUtil {

    protected DataSource dataSource;

    /**
     * 初始化.
     */
    public abstract void init(String driverClass, String url, String userName, String password);

    /**
     * 连接池释放.
     */
    public abstract void close();

    /**
     * 连接池新增配置参数.
     */
    protected abstract void addDataSourceProperty(String propertyName, String value);

    /**
     * 初始化连接池时配置参数 设计时用于生成数据库文档功能用.
     */
    protected void addDataSourceProperties(String driver) {
        switch (DataBaseType.fromTypeName(driver)) {
            case MYSQL:
            case MYSQL8:
            case MARIADB:
                // 设置mysql/mariadb可以获取 tables remarks 信息
                addDataSourceProperty("useInformationSchema", "true");
                addDataSourceProperty("characterEncoding", "UTF-8");
                break;
            case ORACLE:
                // 设置oracle是否获取注释
                addDataSourceProperty("remarksReporting", "true");
                break;
            case SQLSERVER:
            case H2:
            case UNKNOW:
            default:
                break;
        }
    }

    /**
     * 数据库sql执行(表创建等).
     */
    public boolean execute(String sql, Object... obj) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = null;
        boolean x = false;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                ps.setObject(i + 1, obj[i]);
            }
            x = ps.execute();
        } catch (SQLException e) {
            StaticLog.error(e);
        } finally {
            release(conn, ps);
        }
        return x;
    }

    /**
     * 用于数据库增删改.
     */
    public int executeUpdate(String sql, Object... obj) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = null;
        int x = 0;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                ps.setObject(i + 1, obj[i]);
            }
            x = ps.executeUpdate();
        } catch (SQLException e) {
            StaticLog.error(e);
        } finally {
            release(conn, ps);
        }
        return x;
    }

    /**
     * 用于获取表数据个数.
     */
    public int getCount(String sql, Object... obj) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                ps.setObject(i + 1, obj[i]);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            StaticLog.error(e);
        } finally {
            release(conn, ps, rs);
        }
        return count;
    }

    /**
     * 查询返回List容器.
     */
    public List<Map<String, Object>> executeQuery(String sql, Object... obj) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            int paramsIndex = 1;
            for (Object p : obj) {
                ps.setObject(paramsIndex++, p);
            }
            rs = ps.executeQuery();
            // 获得结果集中列的信息
            ResultSetMetaData rst = rs.getMetaData();
            // 获得结果集的列的数量
            int column = rst.getColumnCount();
            // 处理结果
            while (rs.next()) {
                // 创建Map容器存取每一列对应的值
                Map<String, Object> m = new HashMap<String, Object>();
                for (int i = 1; i <= column; i++) {
                    m.put(rst.getColumnName(i), rs.getObject(i));
                }
                list.add(m);
            }
        } catch (SQLException e) {
            StaticLog.error(e);
        } finally {
            release(conn, ps, rs);
        }
        return list;
    }

    /**
     * 释放连接池连接,如果释放不了,就把它的引用置为空,让GC去回收对象.
     */
    public void release(Connection conn, PreparedStatement ps, ResultSet rs, CallableStatement cs) {
        if (cs != null) {
            try {
                cs.close();
            } catch (SQLException e) {
                StaticLog.error(e);
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                StaticLog.error(e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                StaticLog.error(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                StaticLog.error(e);
            }
        }
    }

    /**
     * 释放连接池连接,如果释放不了,就把它的引用置为空,让GC去回收对象.
     */
    public void release(Connection conn, CallableStatement cs) {
        release(conn, null, null, cs);
    }

    /**
     * 释放连接池连接,如果释放不了,就把它的引用置为空,让GC去回收对象.
     */
    public void release(Connection conn, PreparedStatement ps) {
        release(conn, ps, null, null);
    }

    /**
     * 释放连接池连接,如果释放不了,就把它的引用置为空,让GC去回收对象.
     */
    public void release(Connection conn, PreparedStatement ps, ResultSet rs) {
        release(conn, ps, rs, null);
    }

    /**
     * 获取缓存数据源信息.
     */
    public String getDataSourceInfo() {
        return dataSource.toString();
    }

    /**
     * 获取连接池.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

}
