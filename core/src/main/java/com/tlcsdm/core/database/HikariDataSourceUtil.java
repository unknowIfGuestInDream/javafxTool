package com.tlcsdm.core.database;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

/**
 * Hikari连接池工具实现类.
 *
 * @author unknowIfGuestInDream
 */
public class HikariDataSourceUtil extends DataSourceUtil {

    private HikariDataSource dataSource;

    public HikariDataSourceUtil() {
        dataSource = new HikariDataSource();
        super.dataSource = dataSource;
    }

    public HikariDataSourceUtil(String driver, String url, String userName, String password) {
        dataSource = new HikariDataSource();
        super.dataSource = dataSource;
        init(driver, url, userName, password);
    }

    @Override
    public void init(String driverClass, String url, String userName, String password) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(driverClass) || StringUtils.isEmpty(userName)
            || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("The database configuration cannot be empty!");
        }
        if (dataSource != null) {
            dataSource.setJdbcUrl(url);
            dataSource.setDriverClassName(driverClass);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
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
        dataSource.addDataSourceProperty(propertyName, value);
    }

    @Override
    public String getDataSourceInfo() {
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        buf.append("\n\tPoolName:\"");
        buf.append(dataSource.getPoolName());
        buf.append(",\n\tTimeout:");
        buf.append(dataSource.getConnectionTimeout());
        buf.append(",\n\tIdleTimeout:");
        buf.append(dataSource.getIdleTimeout());
        buf.append(",\n\tValidationTimeout:");
        buf.append(dataSource.getValidationTimeout());
        buf.append("\n}");
        return buf.toString();
    }

}
