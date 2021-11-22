package com.rishod.oddy.backend;

import static com.rishod.oddy.Profiles.BACKEND;

import com.rishod.oddy.api.TemperatureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Profile(BACKEND)
@Component
public class TemperatureConsumingInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final ReactiveKafkaConsumerTemplate<String, TemperatureData> temperatureDataConsumerTemplate;

    public TemperatureConsumingInitializer(ReactiveKafkaConsumerTemplate<String, TemperatureData> temperatureDataConsumerTemplate) {
        this.temperatureDataConsumerTemplate = temperatureDataConsumerTemplate;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        temperatureDataConsumerTemplate.receiveAutoAck()
                .doOnNext(record -> log.info("Successfully consumed record [key={}, value={} from topic={}, offset={}]",
                        record.key(),
                        record.value(),
                        record.topic(),
                        record.offset()))
                .doOnError(throwable -> log.error("Something bad happened while consuming.", throwable))
                .subscribe();
    }
}
