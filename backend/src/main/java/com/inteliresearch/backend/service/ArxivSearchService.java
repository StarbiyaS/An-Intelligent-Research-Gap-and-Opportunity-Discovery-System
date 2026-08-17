package com.inteliresearch.backend.service;

import com.inteliresearch.backend.dto.ArxivEntry;
import com.inteliresearch.backend.dto.ArxivFeed;
import com.inteliresearch.backend.entity.Paper;
import com.inteliresearch.backend.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArxivSearchService {

    @Autowired
    private PaperRepository paperRepository;

    private final RestClient restClient = RestClient.create();

    public List<Paper> searchAndSavePapers(String topic) {

        ArxivFeed feed = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("export.arxiv.org")
                        .path("/api/query")
                        .queryParam("search_query", "all:" + topic)
                        .queryParam("start", "0")
                        .queryParam("max_results", "10")
                        .build())
                .retrieve()
                .body(ArxivFeed.class);

        List<Paper> savedPapers = new ArrayList<>();

        if (feed != null && feed.getEntries() != null) {
            for (ArxivEntry entry : feed.getEntries()) {
                Paper paper = new Paper();
                paper.setTitle(entry.getTitle() != null ? entry.getTitle().trim() : null);
                paper.setAbstractText(entry.getSummary() != null ? entry.getSummary().trim() : null);
                paper.setUrl(entry.getId());
                paper.setSource("arXiv");

                if (entry.getPublished() != null && entry.getPublished().length() >= 4) {
                    String yearStr = entry.getPublished().substring(0, 4);
                    paper.setYear(Integer.parseInt(yearStr));
                }

                if (entry.getAuthors() != null) {
                    String authorNames = entry.getAuthors().stream()
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
}