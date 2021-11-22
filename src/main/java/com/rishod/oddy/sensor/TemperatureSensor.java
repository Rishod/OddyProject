package com.rishod.oddy.sensor;

import static com.rishod.oddy.Profiles.SENSOR;

import com.rishod.oddy.Endpoints;
import com.rishod.oddy.api.TemperatureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Profile(SENSOR)
@Component
public class TemperatureSensor implements ApplicationRunner {
    private static final Random RANDOM = new Random();

    private final WebClient webClient;
    private final String sourceName;

    public TemperatureSensor(@Value("${frontend.server}") String frontendServer,
                             @Value("${sensor.source.name}") String sourceName) {
        this.webClient = WebClient.builder()
                .baseUrl(frontendServer)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.sourceName = sourceName;
    }

    @Override
    public void run(ApplicationArguments args) {
        this.generateAndPushRandomData();
    }

    public void generateAndPushRandomData() {
        Hooks.onErrorDropped(error -> log.warn("Connection closed"));

        Flux.interval(Duration.ofSeconds(1))
                .map(tick -> this.generateRandomData())
                .doOnNext(data -> log.info("Pushing temperature data [id: {}, value: {}]", data.getId(), data.getTemperature()))
                .doOnError(throwable -> log.error("Something gone wrong no pushing data", throwable))
                .subscribe(data -> webClient.post()
                        .uri(Endpoints.TEMPERATURE)
                        .bodyValue(data)
                        .exchangeToMono(clientResponse -> Mono.empty()).subscribe());

//        Flux<TemperatureData> temperatureDataFlux = Flux.interval(Duration.ofSeconds(1))
//                .map(tick -> this.generateRandomData())
//                .doOnNext(data -> log
//                        .info("Pushing temperature data [id: {}, value: {}]", data.getId(), data.getTemperature()))
//                .doOnError(throwable -> log.error("Something gone wrong no pushing data", throwable));
//
//        webClient.post()
//                .uri(Endpoints.TEMPERATURE)
//                .body(temperatureDataFlux, TemperatureData.class)
//                .exchangeToMono(clientResponse -> Mono.empty())
//                .subscribe();
    }

    public TemperatureData generateRandomData() {
        return TemperatureData.builder()
                .id(UUID.randomUUID())
                .source(sourceName)
                .temperature(new BigDecimal(RANDOM.nextInt()))
                .build();
    }
}
