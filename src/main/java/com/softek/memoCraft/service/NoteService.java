package com.softek.memoCraft.service;

import com.softek.memoCraft.model.TextDocument;
import com.softek.memoCraft.repository.NoteRepository;
import com.softek.memoCraft.model.Note;
import com.softek.memoCraft.repository.TextDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.Set;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TextDocumentRepository textDocumentRepository;

    public Page<Note> getNotesByLabel(int page, int size, Set<String> labels) {
        Pageable pageable = PageRequest.of(page, size);
        return !CollectionUtils.isEmpty(labels)
                ? noteRepository.findByLabels(labels, (long)labels.size(), pageable)
                : noteRepository.findAll(pageable);
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note saveOrUpdate(Note note) {
        return noteRepository.save(note);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public TextDocument getTextDocumentById(String id) {
        return textDocumentRepository.findById(id).orElse(null);
    }

    public TextDocument createOrUpdateTextDocument(TextDocument textDocument) {
        return textDocumentRepository.save(textDocument);
    }

    public void deleteTextDocumentById(String id) {
        textDocumentRepository.deleteById(id);
    }
}
