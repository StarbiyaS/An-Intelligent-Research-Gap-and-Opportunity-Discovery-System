package com.inteliresearch.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SemanticScholarPaper {

    private String title;

    @JsonProperty("abstract")
    private String abstractText;

    private Integer year;
    private String url;
    private List<SemanticScholarAuthor> authors;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public List<SemanticScholarAuthor> getAuthors() { return authors; }
    public void setAuthors(List<SemanticScholarAuthor> authors) { this.authors = authors; }
}