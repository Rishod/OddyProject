package com.rishod.oddy.statistic;

import static com.rishod.oddy.Profiles.STATISTIC_CALCULATOR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile(STATISTIC_CALCULATOR)
@Component
public class StatisticGenerationRunner implements ApplicationRunner {

    private final StatisticGenerationJob statisticGenerationJob;

    public StatisticGenerationRunner(StatisticGenerationJob statisticGenerationJob) {
        this.statisticGenerationJob = statisticGenerationJob;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        statisticGenerationJob.generateStatistic();
    }
}
