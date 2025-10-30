package com.encurtador.url.service;

import java.time.Instant;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.encurtador.url.dto.ShortenedUrlDTO;
import com.encurtador.url.model.UrlModel;
import com.encurtador.url.repository.UrlRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UrlService {

    @Value("${hashids.salt}")
    private String hashSalt;

    @Value("${hashids.min-hash-length}")
    private Integer hashLength;

    @Value("${hashids.alphabet}")
    private String alphabet;

    @Value("${app.domain}")
    private String domain;

    private Hashids hashids;

    @Autowired
    private UrlRepository repository;

    @PostConstruct
    public void init() {
        this.hashids = new Hashids(hashSalt, hashLength, alphabet);
    }

    public ShortenedUrlDTO getShortenedUrl(String originalUrl) {

        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("A URL não pode ser nula ou vazia");
        }

        originalUrl = originalUrl.replaceAll("^\"|\"$", "");

        if (!UrlValidator.isValidHttpsUrl(originalUrl)) {
            throw new IllegalArgumentException("URL inválida: " + originalUrl);
        }

        String hash = this.hashids.encode(Math.abs(originalUrl.hashCode()));
        String urlShort = String.format("%s/%s", domain, hash);

        ShortenedUrlDTO dto = new ShortenedUrlDTO();
        dto.setShortenedUrl(urlShort);

        try {
            UrlModel urlModel = new UrlModel(hash, originalUrl, Instant.now());
            this.repository.save(urlModel);
            return dto;

        } catch (DuplicateKeyException e) {
            return dto;
        }
    }

    public String getOriginalUrl(String hash) {
        return this.repository.findByShortHash(hash)
                .map(UrlModel::getLongUrl)
                .orElse(null);
    }
}
