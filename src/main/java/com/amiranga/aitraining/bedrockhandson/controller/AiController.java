package com.amiranga.aitraining.bedrockhandson.controller;

import com.amiranga.aitraining.bedrockhandson.service.LocalEmbedderService;
import com.amiranga.aitraining.bedrockhandson.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final RagService ragService;
    private final LocalEmbedderService embedder;

    public AiController(RagService ragService, LocalEmbedderService embedder) {
        this.ragService = ragService;
        this.embedder = embedder;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody PromptDto request) throws Exception {
        return ragService.query(request.getPrompt());
    }

    /**
     * API to index a single text input
     */
    @PostMapping("/indexText")
    public String indexText(@RequestBody IndexTextRequest request) {
        try {
            embedder.indexText(request.getText());
            return "Text indexed successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Indexing failed: " + e.getMessage();
        }
    }

    public static class PromptDto {
        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }

    public static class IndexTextRequest {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
