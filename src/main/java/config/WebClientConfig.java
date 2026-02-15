package com.uniremington.semillero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${ms.docentes.url:http://localhost:8081}")
    private String docentesUrl;

    @Value("${ms.eventos.url:http://localhost:8082}")
    private String eventosUrl;

    @Bean
    public WebClient docentesWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5));

        return WebClient.builder()
                .baseUrl(docentesUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean(name = "eventosWebClient")
    public WebClient eventosWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5));

        return WebClient.builder()
                .baseUrl(eventosUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}