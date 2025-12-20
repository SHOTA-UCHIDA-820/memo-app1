package com.example.memoapp1.service;

import com.example.memoapp1.entity.Memos;
import com.example.memoapp1.entity.Tags;
import com.example.memoapp1.repository.MemoRepository;
import com.example.memoapp1.specification.MemoSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    @Transactional(readOnly = true)
    public Memos findById(Long id) {
        return memoRepository.findByIdWithTags(id);
    }

    @Transactional(readOnly = true)
    public List<Memos> findAll() {
        return memoRepository.findAllWithTags();
    }

    @Transactional(readOnly = true)
    public List<Memos> searchMemos(String keyword, LocalDate startDate, LocalDate endDate, Set<String> tagNames, String sortOrder) {

        Specification<Memos> spec = Specification.allOf(
                MemoSpecification.containsKeyword(keyword),
                MemoSpecification.createdAfter(startDate),
                MemoSpecification.createdBefore(endDate),
                MemoSpecification.hasTags(tagNames)
        );

        Sort sort = "desc".equalsIgnoreCase(sortOrder)
                ? Sort.by("createdAt").descending()
                : Sort.by("createdAt").ascending();

        return memoRepository.findAll(spec, sort);
    }

    @Transactional
    public Memos save(Memos memo) {
        return memoRepository.save(memo);
    }

    @Transactional
    public Memos update(Memos memo, Set<Tags> tags) {
        memo.setTags(tags);
        return memoRepository.save(memo);
    }

    @Transactional
    public void delete(Long id) {
        memoRepository.deleteById(id);
    }
}









