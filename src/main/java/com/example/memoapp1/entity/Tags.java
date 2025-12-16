package com.example.memoapp1.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToMany(mappedBy = "tags")
    private Set<Memos> memos = new HashSet<>();

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public Set<Memos> getMemos() { return memos; }
    public void setMemos(Set<Memos> memos) { this.memos = memos; }
}
