package com.amiranga.aitraining.bedrockhandson.service;

import com.amiranga.aitraining.bedrockhandson.model.DocumentChunk;
import com.amiranga.aitraining.bedrockhandson.repository.DocumentChunkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.util.*;

@Service
public class LocalEmbedderService {

    private final DocumentChunkRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public LocalEmbedderService(DocumentChunkRepository repository) {
        this.repository = repository;
    }

    private float[] computeEmbedding(String text) {
        // Simple placeholder embedding: use hash + random values for demo
        Random rand = new Random(text.hashCode());
        float[] embedding = new float[512];
        for (int i = 0; i < 512; i++) embedding[i] = rand.nextFloat();
        return embedding;
    }

    public void indexDocuments(String folderPath) throws Exception {
        Files.list(Paths.get(folderPath)).filter(Files::isRegularFile).forEach(file -> {
            try {
                String content = Files.readString(file);
                int chunkSize = 500;
                for (int i = 0; i < content.length(); i += chunkSize) {
                    String chunk = content.substring(i, Math.min(i + chunkSize, content.length()));
                    String embJson = mapper.writeValueAsString(computeEmbedding(chunk));
                    repository.save(DocumentChunk.builder().chunkText(chunk).embeddingJson(embJson).build());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void indexText(String text) throws Exception {
        int chunkSize = 500;
        for (int i = 0; i < text.length(); i += chunkSize) {
            String chunk = text.substring(i, Math.min(i + chunkSize, text.length()));
            String embJson = mapper.writeValueAsString(computeEmbedding(chunk));
            repository.save(DocumentChunk.builder().chunkText(chunk).embeddingJson(embJson).build());
        }
    }

    public float[] embedQuery(String query) {
        return computeEmbedding(query);
    }
}

