package com.inteliresearch.backend.controller;

import com.inteliresearch.backend.entity.Paper;
import com.inteliresearch.backend.repository.PaperRepository;
import com.inteliresearch.backend.service.PaperSearchService;
import com.inteliresearch.backend.service.ArxivSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/papers")
public class PaperController {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperSearchService paperSearchService;

    @Autowired
    private ArxivSearchService arxivSearchService;

    @PostMapping
    public Paper createPaper(@RequestBody Paper paper) {
        return paperRepository.save(paper);
    }

    @GetMapping
    public List<Paper> getAllPapers() {
        return paperRepository.findAll();
    }

    @GetMapping("/search")
    public List<Paper> searchPapers(@RequestParam String topic) {
        return paperSearchService.searchAndSavePapers(topic);
    }

    @GetMapping("/search/arxiv")
    public List<Paper> searchArxivPapers(@RequestParam String topic) {
        return arxivSearchService.searchAndSavePapers(topic);
    }
}