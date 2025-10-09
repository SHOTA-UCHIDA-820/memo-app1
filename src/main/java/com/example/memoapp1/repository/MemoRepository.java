package com.example.memoapp1.repository;

import com.example.memoapp1.entity.Memos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memos, Long> {

    @Query("SELECT m FROM Memos m LEFT JOIN FETCH m.tags WHERE m.id = :id")
    Memos findByIdWithTags(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Memos m LEFT JOIN FETCH m.tags")
    List<Memos> findAllWithTags();
}
