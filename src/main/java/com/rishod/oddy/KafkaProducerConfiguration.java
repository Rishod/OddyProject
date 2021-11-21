package com.rishod.oddy;

import static com.rishod.oddy.Profiles.FRONTEND;

import com.rishod.oddy.api.TemperatureData;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Profile(FRONTEND)
@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public ReactiveKafkaProducerTemplate<String, TemperatureData> temperatureDataProducerTemplate(
            final KafkaProperties properties) {
        final Map<String, Object> props = properties.buildProducerProperties();

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(props));
    }

}
