package com.rishod.oddy.statistic;

import static com.rishod.oddy.Profiles.SCHEDULED_STATISTIC_CALCULATOR;
import static com.rishod.oddy.Profiles.STATISTIC_CALCULATOR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({STATISTIC_CALCULATOR, SCHEDULED_STATISTIC_CALCULATOR})
@Component
public class StatisticGenerationJob {

    private final String helloMessage;

    public StatisticGenerationJob(@Value("${hello.message}") String helloMessage) {
        this.helloMessage = helloMessage;
    }

    public void generateStatistic() {
        log.info(helloMessage);
    }

}
