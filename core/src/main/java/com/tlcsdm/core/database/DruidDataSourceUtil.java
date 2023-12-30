package com.tlcsdm.core.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;

/**
 * Druid连接池工具实现类.
 *
 * @author unknowIfGuestInDream
 */
public class DruidDataSourceUtil extends DataSourceUtil {

    private DruidDataSource dataSource;

    public DruidDataSourceUtil() {
        dataSource = new DruidDataSource();
        super.dataSource = dataSource;
    }

    public DruidDataSourceUtil(String driver, String url, String userName, String password) {
        dataSource = new DruidDataSource();
        super.dataSource = dataSource;
        init(driver, url, userName, password);
    }

    @Override
    public void init(String driverClass, String url, String userName, String password) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(driverClass) || StringUtils.isEmpty(userName)
            || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("The database configuration cannot be empty!");
        }
        if (dataSource != null && !dataSource.isInited()) {
            dataSource.setUrl(url);
            dataSource.setDriverClassName(driverClass);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            dataSource.setBreakAfterAcquireFailure(true);
            addDataSourceProperties(driverClass);
        }
    }

    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
        dataSource = null;
    }

    @Override
    protected void addDataSourceProperty(String propertyName, String value) {
        dataSource.addConnectionProperty(propertyName, value);
    }

}
