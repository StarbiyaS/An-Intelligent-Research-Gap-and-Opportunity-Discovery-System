package com.inteliresearch.backend.service;

import com.inteliresearch.backend.entity.Limitation;
import com.inteliresearch.backend.entity.Paper;
import com.inteliresearch.backend.repository.LimitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LimitationExtractionService {

    @Autowired
    private LimitationRepository limitationRepository;

    private static final List<String> LIMITATION_KEYWORDS = Arrays.asList(
            "small dataset", "limited dataset", "small sample",
            "lack of", "lacks",
            "limited generalization", "does not generalize", "poor generalization",
            "limited explainability", "lack of interpretability", "black box",
            "high computational cost", "computationally expensive",
            "remains a challenge", "remains challenging",
            "limitation", "limitations"
    );

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
                    break; // avoid saving the same sentence twice for multiple keyword matches
                }
            }
        }

        return found;
    }
}