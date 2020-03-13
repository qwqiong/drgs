package com.dchealth.drgs.config.druid;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author qiaowenqiong
 * @Date 2020/3/12 下午7:51
 **/
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.dchealth.drgs.db"})//设置dao（repo）所在位置
@EntityScan(basePackages = {"com.dchealth.drgs.db.entity", "org.springframework.data.jpa.convert.threeten"})
public class DBConfig {
}
