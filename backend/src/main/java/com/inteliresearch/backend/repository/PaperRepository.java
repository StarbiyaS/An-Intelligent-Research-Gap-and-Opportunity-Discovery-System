package com.inteliresearch.backend.repository;

import com.inteliresearch.backend.entity.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, Long> {
}