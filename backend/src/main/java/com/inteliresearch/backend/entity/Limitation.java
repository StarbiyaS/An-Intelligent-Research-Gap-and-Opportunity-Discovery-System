package com.inteliresearch.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "limitations")
public class Limitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @Column(columnDefinition = "TEXT")
    private String limitationText;

    private String category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public String getLimitationText() {
        return limitationText;
    }

    public void setLimitationText(String limitationText) {
        this.limitationText = limitationText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}