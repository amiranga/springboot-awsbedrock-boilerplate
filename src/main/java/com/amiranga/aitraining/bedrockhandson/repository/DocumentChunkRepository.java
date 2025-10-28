package com.amiranga.aitraining.bedrockhandson.repository;


import com.amiranga.aitraining.bedrockhandson.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {
}
