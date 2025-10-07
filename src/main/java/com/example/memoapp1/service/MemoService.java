package com.example.memoapp1.service;

import com.example.memoapp1.entity.Memo;
import com.example.memoapp1.entity.Tag;
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
    public Memo getMemoById(Long id) {
        return memoRepository.findByIdWithTags(id);
    }

    @Transactional(readOnly = true)
    public List<Memo> getAllMemos() {
        return memoRepository.findAllWithTags();
    }

    @Transactional
    public Memo createMemo(Memo memo) {
        return memoRepository.save(memo);
    }

    @Transactional
    public Memo updateMemo(Memo memo, Set<Tag> tags) {
        memo.setTags(tags);
        return memoRepository.save(memo);
    }

    @Transactional
    public void deleteMemo(Long id) {
        memoRepository.deleteById(id);
    }
}

