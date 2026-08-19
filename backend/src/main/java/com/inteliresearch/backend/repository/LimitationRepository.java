package com.inteliresearch.backend.repository;

import com.inteliresearch.backend.entity.Limitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LimitationRepository extends JpaRepository<Limitation, Long> {

    List<Limitation> findByPaperId(Long paperId);
}