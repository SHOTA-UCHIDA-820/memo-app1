package com.example.memoapp1.service;

import com.example.memoapp1.entity.Tags;
import com.example.memoapp1.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public List<Tags> getAllTags() {
        return tagRepository.findAll(Sort.by("name").ascending());
    }

    @Transactional(readOnly = true)
    public Tags getTagById(Long id) {
        Optional<Tags> tag = tagRepository.findById(id);
        return tag.orElse(null);
    }
}



