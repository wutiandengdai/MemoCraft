package com.softek.memoCraft.controller;

import com.softek.memoCraft.model.Note;
import com.softek.memoCraft.model.TextDocument;
import com.softek.memoCraft.service.NoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public Page<Note> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Set<String> labels) {
        return noteService.getNotesByLabel(page, size, labels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id){
        Optional<Note> note = noteService.getNoteById(id);
        return note.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        // Create a new TextDocument in MongoDB
        TextDocument textDocument = new TextDocument(note.getTextRef());
        TextDocument createdTextDocument = noteService.createOrUpdateTextDocument(textDocument);

        // Set the textRef to the ID of the created TextDocument
        note.setTextRef(createdTextDocument.getId());

        // Save the note in PostgreSQL
        Note createdNote = noteService.saveOrUpdate(note);

        return ResponseEntity.ok(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
        if (noteService.getNoteById(id).isPresent()) {
            note.setId(id);

            // Update the TextDocument in MongoDB
            TextDocument textDocument = new TextDocument(note.getTextRef());
            textDocument.setId(note.getTextRef());
            noteService.createOrUpdateTextDocument(textDocument);

            // Save the note in PostgreSQL
            return ResponseEntity.ok(noteService.saveOrUpdate(note));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent()) {
            // Delete the TextDocument in MongoDB
            noteService.deleteTextDocumentById(note.get().getTextRef());

            // Delete the note in PostgreSQL
            noteService.deleteNoteById(id);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/text/{id}")
    public ResponseEntity<TextDocument> getTextDocumentById(@PathVariable String id) {
        TextDocument textDocument = noteService.getTextDocumentById(id);
        if (textDocument != null) {
            return ResponseEntity.ok(textDocument);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/text/{id}/stats")
    public ResponseEntity<Map<String, Long>> getNoteTextStats(@PathVariable String id) {
        TextDocument textDocument = noteService.getTextDocumentById(id);
        if (textDocument != null) {
            String noteText = textDocument.getText();
            if (!StringUtils.isBlank(noteText)) {
                Map<String, Long> stats =
                        Arrays.stream(noteText.split("")).
                                collect(Collectors.groupingBy(c -> c, Collectors.counting()));
                return ResponseEntity.ok(stats);
            }
        }
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/text")
    public TextDocument createTextDocument(@RequestBody TextDocument textDocument) {
        return noteService.createOrUpdateTextDocument(textDocument);
    }

    @PutMapping("/text/{id}")
    public ResponseEntity<TextDocument> updateTextDocument(@PathVariable String id, @RequestBody TextDocument textDocument) {
        if (noteService.getTextDocumentById(id) != null) {
            textDocument.setId(id);
            return ResponseEntity.ok(noteService.createOrUpdateTextDocument(textDocument));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/text/{id}")
    public ResponseEntity<Void> deleteTextDocument(@PathVariable String id) {
        if (noteService.getTextDocumentById(id) != null) {
            noteService.deleteTextDocumentById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
