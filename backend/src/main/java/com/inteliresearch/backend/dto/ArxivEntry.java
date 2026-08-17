package com.inteliresearch.backend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class ArxivEntry {

    @JacksonXmlProperty(localName = "title", namespace = "http://www.w3.org/2005/Atom")
    private String title;

    @JacksonXmlProperty(localName = "summary", namespace = "http://www.w3.org/2005/Atom")
    private String summary;

    @JacksonXmlProperty(localName = "published", namespace = "http://www.w3.org/2005/Atom")
    private String published;

    @JacksonXmlProperty(localName = "id", namespace = "http://www.w3.org/2005/Atom")
    private String id;

    @JacksonXmlProperty(localName = "author", namespace = "http://www.w3.org/2005/Atom")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ArxivAuthor> authors;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getPublished() { return published; }
    public void setPublished(String published) { this.published = published; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<ArxivAuthor> getAuthors() { return authors; }
    public void setAuthors(List<ArxivAuthor> authors) { this.authors = authors; }
}