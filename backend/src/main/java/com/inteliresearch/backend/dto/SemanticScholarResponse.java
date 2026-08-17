package com.inteliresearch.backend.dto;

import java.util.List;

public class SemanticScholarResponse {

    private List<SemanticScholarPaper> data;

    public List<SemanticScholarPaper> getData() {
        return data;
    }

    public void setData(List<SemanticScholarPaper> data) {
        this.data = data;
    }
}