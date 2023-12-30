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

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.util.DependencyUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 数据库连接池工厂 url+name作为主键.
 *
 * @author unknowIfGuestInDream
 */
public class DataSourceUtilFactory {

    private static final Map<String, DataSourceUtil> strategyMap = new ConcurrentHashMap<>();

    /**
     * 获取strategyMap中所有DataSourceUtil的连接池信息.
     */
    public static List<String> getDataSourceInfoList() {
        return strategyMap.values().stream().map(DataSourceUtil::getDataSourceInfo).collect(Collectors.toList());
    }

    /**
     * 获取DataSourceUtil连接池工具类.
     */
    public static DataSourceUtil getDataSourceUtil(String key) {
        return strategyMap.get(key);
    }

    /**
     * 获取DataSourceUtil连接池工具类，为null则初始化.
     */
    public static DataSourceUtil getDataSourceUtil(String url, String driver, String userName, String password) {
        return getDataSourceUtil(url, driver, userName, password, DataSourceUtilTypes.HIKARI);
    }

    /**
     * 获取DataSourceUtil连接池工具类，为null则初始化.
     */
    public static DataSourceUtil getDataSourceUtil(String url, String driver, String userName, String password,
        int dataSourceUtilType) {
        return Optional.ofNullable(DataSourceUtilFactory.getDataSourceUtil(url + userName))
            .orElseGet(() -> createDataSourceUtil(url, driver, userName, password, dataSourceUtilType));
    }

    /**
     * 创建DataSourceUtil并注册到strategyMap中.
     */
    private static DataSourceUtil createDataSourceUtil(String url, String driver, String userName, String password,
        int dataSourceUtilType) {
        DataSourceUtil dataSourceUtil = null;
        try {
            dataSourceUtil = switch (dataSourceUtilType) {
                case DataSourceUtilTypes.DRUID -> {
                    if (DependencyUtil.hasDruid()) {
                        yield new DruidDataSourceUtil(driver, url, userName, password);
                    } else {
                        throw new UnExpectedResultException("Not found Druid!");
                    }
                }
                default -> {
                    if (DependencyUtil.hasHikari()) {
                        yield new HikariDataSourceUtil(driver, url, userName, password);
                    } else {
                        throw new UnExpectedResultException("Not found Hikari!");
                    }
                }
            };
            register(url + userName, dataSourceUtil);
        } catch (Exception e) {
            StaticLog.error(e);
            remove(url + userName);
        }
        return dataSourceUtil;
    }

    /**
     * 将DataSourceUtil注册到strategyMap中.
     */
    public static void register(String name, DataSourceUtil handler) {
        if (StringUtils.isEmpty(name) || null == handler) {
            return;
        }
        strategyMap.put(name, handler);
    }

    /**
     * 删除strategyMap指定值.
     */
    public static void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        Optional.ofNullable(strategyMap.remove(key)).ifPresent(DataSourceUtil::close);
    }

    /**
     * 删除strategyMap所有值.
     */
    public static void removeAll() {
        strategyMap.values().forEach(DataSourceUtil::close);
        strategyMap.clear();
    }

}
