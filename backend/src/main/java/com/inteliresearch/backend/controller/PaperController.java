package com.inteliresearch.backend.controller;

import com.inteliresearch.backend.entity.Paper;
import com.inteliresearch.backend.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/papers")
public class PaperController {

    @Autowired
    private PaperRepository paperRepository;

    @PostMapping
    public Paper createPaper(@RequestBody Paper paper) {
        return paperRepository.save(paper);
    }

    @GetMapping
    public List<Paper> getAllPapers() {
        return paperRepository.findAll();
    }
}