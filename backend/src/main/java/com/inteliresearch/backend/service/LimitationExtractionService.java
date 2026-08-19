package com.inteliresearch.backend.service;

import com.inteliresearch.backend.dto.GeminiRequest;
import com.inteliresearch.backend.dto.GeminiResponse;
import com.inteliresearch.backend.entity.Limitation;
import com.inteliresearch.backend.entity.Paper;
import com.inteliresearch.backend.repository.LimitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LimitationExtractionService {

    @Autowired
    private LimitationRepository limitationRepository;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestClient restClient = RestClient.create();

    private static final List<String> LIMITATION_KEYWORDS = Arrays.asList(
            "small dataset", "limited dataset", "small sample",
            "lack of", "lacks",
            "limited generalization", "does not generalize", "poor generalization",
            "limited explainability", "lack of interpretability", "black box",
            "high computational cost", "computationally expensive",
            "remains a challenge", "remains challenging",
            "limitation", "limitations"
    );

    // ===== KEYWORD-BASED METHOD (single paper) =====
    public List<Limitation> extractLimitations(Paper paper) {
        List<Limitation> found = new ArrayList<>();

        if (paper.getAbstractText() == null || paper.getAbstractText().isEmpty()) {
            return found;
        }

        String[] sentences = paper.getAbstractText().split("(?<=[.!?])\\s+");

        for (String sentence : sentences) {
            String lowerSentence = sentence.toLowerCase();

            for (String keyword : LIMITATION_KEYWORDS) {
                if (lowerSentence.contains(keyword)) {
                    Limitation limitation = new Limitation();
                    limitation.setPaper(paper);
                    limitation.setLimitationText(sentence.trim());
                    limitation.setCategory(keyword);

                    Limitation saved = limitationRepository.save(limitation);
                    found.add(saved);
                    break;
                }
            }
        }

        return found;
    }

    // ===== LLM-BASED METHOD (single paper) =====
    public List<Limitation> extractLimitationsWithLLM(Paper paper) {
        List<Limitation> found = new ArrayList<>();

        if (paper.getAbstractText() == null || paper.getAbstractText().isEmpty()) {
            return found;
        }

        String prompt = """
                You are analyzing a research paper abstract to identify explicitly stated limitations.

                Extract only limitations that are directly stated or clearly implied in the text below.
                Do not invent limitations that aren't supported by the text.
                If no limitations are stated, respond with exactly: NONE

                Return each limitation as a separate line, with no numbering, bullets, or extra commentary.

                Abstract:
                \"\"\"
                %s
                \"\"\"
                """.formatted(paper.getAbstractText());

        GeminiRequest request = new GeminiRequest(prompt);

        GeminiResponse response = restClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent")
                .header("x-goog-api-key", geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);

        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            String rawText = response.getCandidates().get(0).getContent().getParts().get(0).getText();

            if (rawText != null && !rawText.trim().equalsIgnoreCase("NONE")) {
                String[] lines = rawText.split("\\r?\\n");

                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) {
                        Limitation limitation = new Limitation();
                        limitation.setPaper(paper);
                        limitation.setLimitationText(trimmed);
                        limitation.setCategory("llm-extracted");

                        Limitation saved = limitationRepository.save(limitation);
                        found.add(saved);
                    }
                }
            }
        }

        return found;
    }

    // ===== LLM-BASED METHOD ACROSS MULTIPLE PAPERS (resilient) =====
    public List<Limitation> extractLimitationsForAllPapers(List<Paper> papers) {
        List<Limitation> allLimitations = new ArrayList<>();

        for (Paper paper : papers) {
            try {
                List<Limitation> paperLimitations = extractLimitationsWithLLM(paper);
                allLimitations.addAll(paperLimitations);
            } catch (Exception e) {
                System.out.println("Skipping paper id " + paper.getId() + " due to error: " + e.getMessage());
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        return allLimitations;
    }
}