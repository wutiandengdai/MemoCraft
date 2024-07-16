package com.softek.memoCraft.model;

import com.softek.memoCraft.util.NoteTag;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.Set;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty(message = "Note title should not be empty")
    private String title;

    @Column
    private Date createdDate;

    @Column
    @NotEmpty(message = "Note text should not be empty")
    private String textRef;

    @ElementCollection(targetClass = NoteTag.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "tags")
    private Set<NoteTag> tags;

    public Note() {
    }

    public Note(String title, Date createdDate, String textRef, Set<NoteTag> tags) {
        this.title = title;
        this.createdDate = createdDate;
        this.textRef = textRef;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getTextRef() {
        return textRef;
    }

    public void setTextRef(String textRef) {
        this.textRef = textRef;
    }

    public Set<NoteTag> getTags() {
        return tags;
    }

    public void setTags(Set<NoteTag> tags) {
        this.tags = tags;
    }
}
