package com.rishod.oddy.statistic;

import static com.rishod.oddy.Profiles.SCHEDULED_STATISTIC_CALCULATOR;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile(SCHEDULED_STATISTIC_CALCULATOR)
@Configuration
@EnableScheduling
public class SchedulingConfiguration {}
