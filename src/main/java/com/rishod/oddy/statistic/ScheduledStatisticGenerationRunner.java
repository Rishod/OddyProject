package com.rishod.oddy.statistic;

import static com.rishod.oddy.Profiles.SCHEDULED_STATISTIC_CALCULATOR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Profile(SCHEDULED_STATISTIC_CALCULATOR)
@Component
public class ScheduledStatisticGenerationRunner {

    private final StatisticGenerationJob statisticGenerationJob;

    public ScheduledStatisticGenerationRunner(StatisticGenerationJob statisticGenerationJob) {
        this.statisticGenerationJob = statisticGenerationJob;
    }

    @Scheduled(fixedDelayString = "${fixedDelay.milliseconds}")
    public void schedule() {
        statisticGenerationJob.generateStatistic();
    }
}
