package com.uniremington.semillero.service;

import com.uniremington.semillero.model.DocenteDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class DocenteClientService {

    private final WebClient webClient;

    public DocenteClientService(@Qualifier("docentesWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<DocenteDTO> listarTodos() {
        try {
            return webClient.get()
                    .uri("/docentes")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(new ParameterizedTypeReference<List<DocenteDTO>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Collections.emptyList())
                    .block();
        } catch (Exception e) {
            System.err.println("Error conectando con ms-docentes: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<DocenteDTO> listarPorArea(String area) {
        try {
            return webClient.get()
                    .uri("/docentes/area/{area}", area)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(new ParameterizedTypeReference<List<DocenteDTO>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Collections.emptyList())
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public DocenteDTO buscarPorId(Long id) {
        try {
            return webClient.get()
                    .uri("/docentes/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(DocenteDTO.class)
                    .timeout(Duration.ofSeconds(5))
                    .block(); // No usar onErrorReturn(null) aquí
        } catch (Exception e) {
            return null;
        }
    }

    public DocenteDTO guardar(DocenteDTO docente, MultipartFile foto) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("nombre", docente.getNombre());
        builder.part("area", docente.getArea());
        builder.part("especialidad", docente.getEspecialidad());
        builder.part("email", docente.getEmail() != null ? docente.getEmail() : "");
        builder.part("telefono", docente.getTelefono() != null ? docente.getTelefono() : "");
        builder.part("experienciaAnios", String.valueOf(docente.getExperienciaAnios() != null ? docente.getExperienciaAnios() : 0));
        builder.part("orden", String.valueOf(docente.getOrden() != null ? docente.getOrden() : 0));

        if (foto != null && !foto.isEmpty()) {
            builder.part("foto", foto.getResource())
                    .filename(foto.getOriginalFilename());
        }

        try {
            System.out.println(">>> DOCENTE CLIENT: Enviando petición POST a ms-docentes...");

            // Usar exchangeToMono para manejar mejor la respuesta
            return webClient.post()
                    .uri("/docentes")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            System.out.println(">>> DOCENTE CLIENT: Respuesta exitosa, parseando...");
                            return response.bodyToMono(DocenteDTO.class);
                        } else {
                            System.err.println(">>> DOCENTE CLIENT: Error HTTP " + response.statusCode());
                            return Mono.empty();
                        }
                    })
                    .timeout(Duration.ofSeconds(10))
                    .block();

        } catch (Exception e) {
            System.err.println(">>> DOCENTE CLIENT: Error al guardar docente: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void eliminar(Long id) {
        try {
            webClient.delete()
                    .uri("/docentes/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception e) {
            System.err.println("Error eliminando docente: " + e.getMessage());
        }
    }
}