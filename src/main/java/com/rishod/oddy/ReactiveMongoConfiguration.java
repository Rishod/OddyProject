package com.rishod.oddy;

import static com.rishod.oddy.Profiles.BACKEND;
import static com.rishod.oddy.Profiles.SCHEDULED_STATISTIC_CALCULATOR;
import static com.rishod.oddy.Profiles.STATISTIC_CALCULATOR;

import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile({BACKEND, SCHEDULED_STATISTIC_CALCULATOR, STATISTIC_CALCULATOR})
@Configuration
@Import({MongoReactiveAutoConfiguration.class, MongoReactiveDataAutoConfiguration.class})
@PropertySource("classpath:datasource.properties")
public class ReactiveMongoConfiguration {
}
