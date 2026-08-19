package com.inteliresearch.backend.dto;

import java.util.List;

public class GeminiRequest {

    private List<GeminiContent> contents;

    public GeminiRequest(String promptText) {
        GeminiPart part = new GeminiPart();
        part.setText(promptText);

        GeminiContent content = new GeminiContent();
        content.setParts(List.of(part));

        this.contents = List.of(content);
    }

    public List<GeminiContent> getContents() {
        return contents;
    }

    public void setContents(List<GeminiContent> contents) {
        this.contents = contents;
    }
}