package com.softek.memoCraft.service;

import com.softek.memoCraft.model.TextDocument;
import com.softek.memoCraft.repository.NoteRepository;
import com.softek.memoCraft.model.Note;
import com.softek.memoCraft.repository.TextDocumentRepository;
import com.softek.memoCraft.util.NoteTag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private TextDocumentRepository textDocumentRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    public void testGetAllNotes() {
        Note note1 = new Note("Title1", new Date(), "TextRef1", Set.of(NoteTag.BUSINESS));
        Note note2 = new Note("Title2", new Date(), "TextRef2", Set.of(NoteTag.BUSINESS, NoteTag.PERSONAL));
        List<Note> notes = Arrays.asList(note1, note2);

        when(noteRepository.findAll()).thenReturn(notes);

        List<Note> result = noteService.getNotesByLabel(0, 5, Set.of(NoteTag.BUSINESS.toString())).toList();

        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
    }

    @Test
    public void testGetNoteById() {
        Note note = new Note("Title", new Date(), "TextRef", Set.of(NoteTag.BUSINESS));
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        Note result = noteService.getNoteById(1L).isPresent() ? noteService.getNoteById(1L).get() : null;

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    public void testCreateOrUpdateNote() {
        Note note = new Note("Title", new Date(),  "textRef1", Set.of(NoteTag.BUSINESS));
        when(noteRepository.save(note)).thenReturn(note);

        Note result = noteService.saveOrUpdate(note);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
    }

    @Test
    public void testDeleteNoteById() {
        noteService.deleteNoteById(1L);
        verify(noteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetTextDocumentById() {
        TextDocument textDocument = new TextDocument("Text");
        when(textDocumentRepository.findById("TextRef")).thenReturn(Optional.of(textDocument));

        TextDocument result = noteService.getTextDocumentById("TextRef");

        assertNotNull(result);
        assertEquals("Text", result.getText());
    }

    @Test
    public void testCreateOrUpdateTextDocument() {
        TextDocument textDocument = new TextDocument("Text");
        when(textDocumentRepository.save(textDocument)).thenReturn(textDocument);

        TextDocument result = noteService.createOrUpdateTextDocument(textDocument);

        assertNotNull(result);
        assertEquals("Text", result.getText());
    }

    @Test
    public void testDeleteTextDocumentById() {
        noteService.deleteTextDocumentById("TextRef");
        verify(textDocumentRepository, times(1)).deleteById("TextRef");
    }
}