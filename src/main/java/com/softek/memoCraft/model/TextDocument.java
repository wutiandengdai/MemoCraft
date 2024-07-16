package com.softek.memoCraft.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "textDocuments")
public class TextDocument {

    @Id
    private String id;
    private String text;

    // Constructors, getters, and setters

    public TextDocument() {}

    public TextDocument(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
