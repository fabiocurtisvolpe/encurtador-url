package com.encurtador.url.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.encurtador.url.dto.ShortenedUrlDTO;
import com.encurtador.url.exception.UrlNotFoundException;
import com.encurtador.url.service.UrlService;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    protected final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenedUrlDTO> getShortenedUrl(@RequestBody String originalUrl) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.urlService.getShortenedUrl(originalUrl));
    }

    @GetMapping("/{hash}")
    public RedirectView redirectToOriginalUrl(@PathVariable String hash) {
        String originalUrl = this.urlService.getOriginalUrl(hash);
        if (originalUrl == null) {
            throw new UrlNotFoundException("URL não encontrada para o hash: " + hash);
        }

        return new RedirectView(originalUrl, true); // true = contextRelative
    }
}
