package com.example.memoapp1.specification;

import com.example.memoapp1.entity.Memos;
import org.springframework.data.jpa.domain.Specification;

public class MemoSpecification {

    public static Specification<Memos> containsKeyword(String keyword) {
        return (root, query, builder) -> {
            if (keyword == null || keyword.isBlank()) {
                return builder.conjunction();
            }
            String pattern = "%" + keyword.trim() + "%";
            return builder.or(
                    builder.like(root.get("title"), pattern),
                    builder.like(root.get("content"), pattern)
            );
        };
    }
}