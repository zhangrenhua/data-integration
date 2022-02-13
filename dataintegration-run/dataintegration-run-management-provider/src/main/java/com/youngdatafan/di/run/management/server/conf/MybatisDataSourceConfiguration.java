package com.youngdatafan.di.run.management.server.conf;


import com.youngdatafan.kettle.springboot.core.datasource.DataSourceProperties;
import com.youngdatafan.kettle.springboot.core.jdbc.EngineMetaDataJdbcTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 引擎数据源配置
 *
 * @author gavin
 * @since 2020/2/14 5:42 下午
 */
@Configuration
@MapperScan(value = "com.youngdatafan.di.run.management.**.mapper", sqlSessionTemplateRef = "DeMetaDataSqlSessionTemplate")
public class MybatisDataSourceConfiguration {

    // TODO 先复用 EngineMetaDataJdbcTemplate数据源
    @Bean("DeMetaDataSqlSession")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(EngineMetaDataJdbcTemplate.DS_NAME) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        // 加载xml
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:com/youngdatafan/di/run/management/**/*Mapper.xml"));
        return sessionFactory.getObject();
    }

    @Bean("DeMetaDataSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("DeMetaDataSqlSession") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    private DataSource createDataSource(DataSourceProperties dataSourceProperty) {
        return createHikariDataSource(dataSourceProperty);
    }

    /**
     * 创建Hikari连接池
     */
    private DataSource createHikariDataSource(DataSourceProperties dataSourceProperty) {
        HikariConfig hikariConfig = dataSourceProperty.getHikari();
        hikariConfig.setJdbcUrl(dataSourceProperty.getUrl());
        hikariConfig.setUsername(dataSourceProperty.getUsername());
        hikariConfig.setPassword(dataSourceProperty.getPassword());
        hikariConfig.setDriverClassName(dataSourceProperty.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }

}
