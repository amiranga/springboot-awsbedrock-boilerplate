package com.amiranga.aitraining.bedrockhandson.service;

import com.amiranga.aitraining.bedrockhandson.model.DocumentChunk;
import com.amiranga.aitraining.bedrockhandson.repository.DocumentChunkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VectorSearchService {

    private final DocumentChunkRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public VectorSearchService(DocumentChunkRepository repository) {
        this.repository = repository;
    }

    private float cosineSimilarity(float[] a, float[] b) {
        float dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return (float) (dot / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    public List<DocumentChunk> getTopK(float[] queryEmb, int k) throws Exception {
        List<DocumentChunk> all = repository.findAll();
        List<Pair> scored = new ArrayList<>();
        for (DocumentChunk c : all) {
            float[] emb = mapper.readValue(c.getEmbeddingJson(), float[].class);
            scored.add(new Pair(c, cosineSimilarity(queryEmb, emb)));
        }
        scored.sort((a, b) -> Float.compare(b.score, a.score));
        return scored.stream().limit(k).map(p -> p.chunk).collect(Collectors.toList());
    }

    private static class Pair {
        DocumentChunk chunk;
        float score;

        Pair(DocumentChunk c, float s) {
            chunk = c;
            score = s;
        }
    }
}
