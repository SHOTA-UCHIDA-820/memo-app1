package com.example.memoapp1.repository;

import com.example.memoapp1.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m FROM Memo m LEFT JOIN FETCH m.tags WHERE m.id = :id")
    Memo findByIdWithTags(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Memo m LEFT JOIN FETCH m.tags")
    List<Memo> findAllWithTags();
}
