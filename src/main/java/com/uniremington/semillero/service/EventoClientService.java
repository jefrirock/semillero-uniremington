package com.uniremington.semillero.service;

import com.uniremington.semillero.model.EventoDTO;
import com.uniremington.semillero.model.FotoEventoDTO;
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
import java.util.stream.Collectors;

@Service
public class EventoClientService {

    private final WebClient webClient;

    public EventoClientService(@Qualifier("eventosWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<EventoDTO> listarTodos() {
        try {
            List<EventoDTO> eventos = webClient.get()
                    .uri("/eventos")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(new ParameterizedTypeReference<List<EventoDTO>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Collections.emptyList())
                    .block();

            return agregarUrlCompleta(eventos);
        } catch (Exception e) {
            System.err.println("Error conectando con ms-eventos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<EventoDTO> listarProximos() {
        try {
            List<EventoDTO> eventos = webClient.get()
                    .uri("/eventos/proximos")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(new ParameterizedTypeReference<List<EventoDTO>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Collections.emptyList())
                    .block();

            return agregarUrlCompleta(eventos);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<EventoDTO> listarPorCategoria(String categoria) {
        try {
            List<EventoDTO> eventos = webClient.get()
                    .uri("/eventos/categoria/{categoria}", categoria)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(new ParameterizedTypeReference<List<EventoDTO>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Collections.emptyList())
                    .block();

            return agregarUrlCompleta(eventos);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public EventoDTO buscarPorId(Long id) {
        try {
            EventoDTO evento = webClient.get()
                    .uri("/eventos/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(EventoDTO.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            return agregarUrlCompleta(evento);
        } catch (Exception e) {
            return null;
        }
    }

    public EventoDTO guardar(EventoDTO evento, MultipartFile imagen) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("titulo", evento.getTitulo());
        builder.part("descripcion", evento.getDescripcion());
        builder.part("categoria", evento.getCategoria());
        builder.part("lugar", evento.getLugar() != null ? evento.getLugar() : "");
        builder.part("fecha", evento.getFecha() != null ? evento.getFecha().toString() : "");
        builder.part("horaInicio", evento.getHoraInicio() != null ? evento.getHoraInicio() : "");
        builder.part("horaFin", evento.getHoraFin() != null ? evento.getHoraFin() : "");

        // CORREGIDO: Usar organizadorId en lugar de docenteId
        if (evento.getOrganizadorId() != null) {
            builder.part("organizadorId", String.valueOf(evento.getOrganizadorId()));
        }

        if (imagen != null && !imagen.isEmpty()) {
            builder.part("imagen", imagen.getResource())
                    .filename(imagen.getOriginalFilename());
        }

        try {
            EventoDTO guardado = webClient.post()
                    .uri("/eventos")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return response.bodyToMono(EventoDTO.class);
                        } else {
                            return Mono.empty();
                        }
                    })
                    .timeout(Duration.ofSeconds(10))
                    .block();

            return agregarUrlCompleta(guardado);
        } catch (Exception e) {
            System.err.println("Error al guardar evento: " + e.getMessage());
            return null;
        }
    }

    public void eliminar(Long id) {
        try {
            webClient.delete()
                    .uri("/eventos/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception e) {
            System.err.println("Error eliminando evento: " + e.getMessage());
        }
    }

    public List<FotoEventoDTO> listarFotosPorEvento(Long eventoId) {
        try {
            return webClient.get()
                    .uri("/eventos/{id}/fotos", eventoId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                    .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.empty())
                    .bodyToMono(new ParameterizedTypeReference<List<FotoEventoDTO>>() {})
                    .timeout(Duration.ofSeconds(5))
                    .onErrorReturn(Collections.emptyList())
                    .block();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // ==================== AGREGAR FOTOS A EVENTO ====================

    public List<FotoEventoDTO> agregarFotos(Long eventoId, List<MultipartFile> fotos) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        if (fotos != null && !fotos.isEmpty()) {
            for (MultipartFile foto : fotos) {
                if (!foto.isEmpty()) {
                    builder.part("fotos", foto.getResource())
                            .filename(foto.getOriginalFilename());
                }
            }
        }

        try {
            List<FotoEventoDTO> guardadas = webClient.post()
                    .uri("/eventos/{id}/fotos", eventoId)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .exchangeToMono(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return response.bodyToMono(new ParameterizedTypeReference<List<FotoEventoDTO>>() {});
                        } else {
                            return Mono.empty();
                        }
                    })
                    .timeout(Duration.ofSeconds(10))
                    .onErrorReturn(Collections.emptyList())
                    .block();

            return guardadas != null ? guardadas : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error al agregar fotos al evento: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // ==================== MÉTODO AUXILIAR PARA URL COMPLETA ====================

    private List<EventoDTO> agregarUrlCompleta(List<EventoDTO> eventos) {
        if (eventos == null) return Collections.emptyList();
        return eventos.stream()
                .map(this::agregarUrlCompleta)
                .collect(Collectors.toList());
    }

    private EventoDTO agregarUrlCompleta(EventoDTO evento) {
        if (evento == null) return null;
        if (evento.getImagenUrl() != null && !evento.getImagenUrl().isEmpty()) {
            // Si la URL no empieza con http, agregar el puerto del microservicio
            if (!evento.getImagenUrl().startsWith("http")) {
                evento.setImagenUrl("http://localhost:8082" + evento.getImagenUrl());
            }
        }
        return evento;
    }
}