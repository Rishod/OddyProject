package com.rishod.oddy;

import static com.rishod.oddy.Endpoints.TEMPERATURE;
import static com.rishod.oddy.Profiles.FRONTEND;

import com.rishod.oddy.api.TemperatureData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Profile(FRONTEND)
@RestController
public class FrontendController {

    private final ReactiveKafkaProducerTemplate<String, TemperatureData> temperatureDataKafkaProducerTemplate;
    private final String temperatureKafkaTopic;

    public FrontendController(ReactiveKafkaProducerTemplate<String, TemperatureData> temperatureDataKafkaProducerTemplate,
                              @Value("${temperature-probe.kafka.topic}") String temperatureKafkaTopic) {
        this.temperatureDataKafkaProducerTemplate = temperatureDataKafkaProducerTemplate;
        this.temperatureKafkaTopic = temperatureKafkaTopic;
    }

    @PostMapping(TEMPERATURE)
    public Mono<Void> postTemperature(@RequestBody TemperatureData data) {
        return temperatureDataKafkaProducerTemplate.send(temperatureKafkaTopic, data)
                .doOnError(e -> log.error("Send failed", e))
                .doOnSuccess(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    log.info("Message {} sent successfully, topic-partition={}-{} offset={} timestamp={}",
                            data.getId(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            LocalDateTime.now());
                }).then();
    }
}
