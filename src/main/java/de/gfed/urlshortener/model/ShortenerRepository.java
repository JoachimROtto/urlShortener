package de.gfed.urlshortener.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortenerRepository extends JpaRepository<Shortener, String> {
    List<Shortener> findFirstByShortID(String id);
}
