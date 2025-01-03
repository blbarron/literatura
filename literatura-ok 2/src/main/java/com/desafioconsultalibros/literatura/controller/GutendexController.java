package com.desafioconsultalibros.literatura.controller;

import org.springframework.web.bind.annotation.*;

import com.desafioconsultalibros.literatura.model.GutendexResponse;
import com.desafioconsultalibros.literatura.service.GutendexService;

@RestController
@RequestMapping("/gutendex")
public class GutendexController {

    private final GutendexService gutendexService;

    public GutendexController(GutendexService gutendexService) {
        this.gutendexService = gutendexService;
    }

    @GetMapping("/search")
    public GutendexResponse searchBook(@RequestParam String title) {
        try {
            return gutendexService.searchBooksByTitle(title);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
