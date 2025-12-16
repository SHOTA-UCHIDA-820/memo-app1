package com.example.memoapp1.specification;

import com.example.memoapp1.entity.Memos;
import com.example.memoapp1.entity.Tags;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.SetJoin;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

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

    public static Specification<Memos> createdAfter(LocalDate startDate) {
        return (root, query, builder) -> {
            if (startDate == null) return builder.conjunction();
            LocalDateTime startDateTime = startDate.atStartOfDay();
            return builder.greaterThanOrEqualTo(root.get("createdAt"), startDateTime);
        };
    }

    public static Specification<Memos> createdBefore(LocalDate endDate) {
        return (root, query, builder) -> {
            if (endDate == null) return builder.conjunction();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            return builder.lessThanOrEqualTo(root.get("createdAt"), endDateTime);
        };
    }

    public static Specification<Memos> hasTags(Set<String> tagNames) {
        return (root, query, builder) -> {
            if (tagNames == null || tagNames.isEmpty()) return builder.conjunction();
            query.distinct(true);
            SetJoin<Memos, Tags> tags = root.joinSet("tags");
            return tags.get("name").in(tagNames);
        };
    }
}
