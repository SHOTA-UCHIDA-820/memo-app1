package com.example.memoapp1.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.HashSet;


@Entity
@Table(name = "memos")
public class Memos extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "memo_tags",
        joinColumns = @JoinColumn(name = "memo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tags> tags = new HashSet<>();

    // getter/setter
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Set<Tags> getTags() { return tags; }
    public void setTags(Set<Tags> tags) { this.tags = tags; }
    
    public String getFormattedCreatedAt() {
        if (getCreatedAt() == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        return getCreatedAt().format(formatter);
    }
}





