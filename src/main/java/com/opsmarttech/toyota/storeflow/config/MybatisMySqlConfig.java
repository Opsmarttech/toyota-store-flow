package com.opsmarttech.toyota.storeflow.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"com.opsmarttech.toyota.storeflow.mapper2"}, sqlSessionFactoryRef = "sqlSessionFactory2")
public class MybatisMySqlConfig {

	@Autowired
	@Qualifier("mysql")
	private DataSource ds2;


	@Bean
	public SqlSessionFactory sqlSessionFactory2() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper2/*.xml"));
		factoryBean.setDataSource(ds2); // 使用mysql数据源, 连接mysql库
		return factoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate2() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory2()); // 使用上面配置的Factory
		return template;
	}

}
