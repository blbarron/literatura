package com.desafioconsultalibros.literatura.service;

import com.desafioconsultalibros.literatura.model.GutendexResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GutendexService {

    private final RestTemplate restTemplate;
     // No-argument constructor for non-Spring contexts
     public GutendexService() {
        this.restTemplate = new RestTemplate();
    }

    // Constructor injection for RestTemplate
    public GutendexService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Busca libros por título utilizando el endpoint de Gutendex.
     * 
     * @param title El título del libro a buscar.
     * @return La respuesta de la API de Gutendex.
     */
    public GutendexResponse searchBooksByTitle(String title) {
        // Endpoint para buscar libros por título
        String url = "https://gutendex.com/books?search=" + title;
        try {
            return restTemplate.getForObject(url, GutendexResponse.class);
        } catch (Exception e) {
            System.err.println("Error al realizar la búsqueda: " + e.getMessage());
            throw new RuntimeException("No se pudo completar la búsqueda.");
        }
    }
}
