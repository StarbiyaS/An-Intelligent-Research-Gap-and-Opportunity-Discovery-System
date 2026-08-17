package com.inteliresearch.backend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "feed", namespace = "http://www.w3.org/2005/Atom")
public class ArxivFeed {

    @JacksonXmlProperty(localName = "entry", namespace = "http://www.w3.org/2005/Atom")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ArxivEntry> entries;

    public List<ArxivEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ArxivEntry> entries) {
        this.entries = entries;
    }
}