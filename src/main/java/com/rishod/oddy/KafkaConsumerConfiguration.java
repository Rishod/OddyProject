package com.rishod.oddy;

import static com.rishod.oddy.Profiles.BACKEND;

import com.rishod.oddy.api.TemperatureData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.Map;

@Profile(BACKEND)
@Configuration
public class KafkaConsumerConfiguration {
    @Bean
    public ReceiverOptions<String, TemperatureData> kafkaReceiverOptions(@Value(value = "${temperature-probe.kafka.topic}") String topic,
                                                                         KafkaProperties kafkaProperties) {
        final Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();
        final ReceiverOptions<String, TemperatureData> receiverOptions = ReceiverOptions.create(consumerProperties);

        return receiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, TemperatureData> temperatureDataConsumerTemplate(
            ReceiverOptions<String, TemperatureData> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}
