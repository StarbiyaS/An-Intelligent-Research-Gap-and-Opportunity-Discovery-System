package com.inteliresearch.backend.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ArxivAuthor {

    @JacksonXmlProperty(localName = "name", namespace = "http://www.w3.org/2005/Atom")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}