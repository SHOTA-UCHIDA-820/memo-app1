package com.example.memoapp1.service;

import com.example.memoapp1.entity.Tag;
import com.example.memoapp1.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public List<Tag> getAllActiveTags() {
        return tagRepository.findByIsDeletedFalse();
    }

    @Transactional(readOnly = true)
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .filter(tag -> !tag.isDeleted()) 
                .orElse(null); 
    }
}