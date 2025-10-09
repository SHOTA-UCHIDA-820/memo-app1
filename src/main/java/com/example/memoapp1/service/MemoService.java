package com.example.memoapp1.service;

import com.example.memoapp1.entity.Memos;
import com.example.memoapp1.entity.Tags;
import com.example.memoapp1.repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

