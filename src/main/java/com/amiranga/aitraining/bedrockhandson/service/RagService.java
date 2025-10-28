package com.amiranga.aitraining.bedrockhandson.service;

import com.amiranga.aitraining.bedrockhandson.model.DocumentChunk;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final ChatModel chatModel;
    private final LocalEmbedderService embedder;
    private final VectorSearchService vectorSearch;

    public RagService(ChatModel chatModel, LocalEmbedderService embedder, VectorSearchService vectorSearch) {
        this.chatModel = chatModel;
        this.embedder = embedder;
        this.vectorSearch = vectorSearch;
    }

    public String query(String userQuery) throws Exception {
        float[] queryEmb = embedder.embedQuery(userQuery);
        List<String> context = vectorSearch.getTopK(queryEmb, 5).stream().map(DocumentChunk::getChunkText).collect(Collectors.toList());

        String promptText = "Answer the question using the context below:\n" + String.join("\n---\n", context) + "\nQuestion: " + userQuery;

        ChatResponse response = chatModel.call(new Prompt(new UserMessage(promptText)));
        return response.getResult().getOutput().getText();
    }
}
