package com.inteliresearch.backend.service;

import com.inteliresearch.backend.dto.SemanticScholarPaper;
import com.inteliresearch.backend.dto.SemanticScholarResponse;
import com.inteliresearch.backend.entity.Paper;
import com.inteliresearch.backend.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaperSearchService {

    @Autowired
    private PaperRepository paperRepository;

    private final RestClient restClient = RestClient.create();

    public List<Paper> searchAndSavePapers(String topic) {

        String url = "https://api.semanticscholar.org/graph/v1/paper/search"
                + "?query=" + topic
                + "&fields=title,abstract,year,url,authors"
                + "&limit=10";

        SemanticScholarResponse response = callWithRetry(url, 3);

        List<Paper> savedPapers = new ArrayList<>();

        if (response != null && response.getData() != null) {
            for (SemanticScholarPaper ssPaper : response.getData()) {
                Paper paper = new Paper();
                paper.setTitle(ssPaper.getTitle());
                paper.setAbstractText(ssPaper.getAbstractText());
                paper.setYear(ssPaper.getYear());
                paper.setUrl(ssPaper.getUrl());
                paper.setSource("Semantic Scholar");

                if (ssPaper.getAuthors() != null) {
                    String authorNames = ssPaper.getAuthors().stream()
                            .map(a -> a.getName())
                            .collect(Collectors.joining(", "));
                    paper.setAuthors(authorNames);
                }

                Paper saved = paperRepository.save(paper);
                savedPapers.add(saved);
            }
        }

        return savedPapers;
    }

    private SemanticScholarResponse callWithRetry(String url, int maxAttempts) {
        int attempt = 0;
        long delay = 2000;

        while (attempt < maxAttempts) {
            try {
                return restClient.get()
                        .uri(url)
                        .retrieve()
                        .body(SemanticScholarResponse.class);
            } catch (HttpClientErrorException.TooManyRequests e) {
                attempt++;
                if (attempt >= maxAttempts) {
                    throw new RuntimeException("Semantic Scholar rate limit reached after " + maxAttempts + " attempts. Please wait a few minutes and try again.");
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ignored) {}
                delay *= 2;
            }
        }
        return null;
    }
}