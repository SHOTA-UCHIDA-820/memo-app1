package com.example.memoapp1.repository;

import com.example.memoapp1.entity.Memos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memos, Long>, JpaSpecificationExecutor<Memos> {

    @Query("SELECT m FROM Memos m LEFT JOIN FETCH m.tags WHERE m.id = :id")
    Memos findByIdWithTags(Long id);

    @Query("SELECT DISTINCT m FROM Memos m LEFT JOIN FETCH m.tags")
    List<Memos> findAllWithTags();
}

