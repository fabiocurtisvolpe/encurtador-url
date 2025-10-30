package com.encurtador.url.repository;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.encurtador.url.model.UrlModel;

@Repository
public interface UrlRepository extends CassandraRepository<UrlModel, String> {

    @Query("SELECT * FROM tb_url WHERE short_url = ?0 ALLOW FILTERING")
    Optional<UrlModel> findByShortHash(String shortHash);

    boolean existsByShortUrl(String shortUrl);
}
