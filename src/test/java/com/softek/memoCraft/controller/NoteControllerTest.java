package com.softek.memoCraft.controller;

import com.softek.memoCraft.model.Note;
import com.softek.memoCraft.model.TextDocument;
import com.softek.memoCraft.service.NoteService;
import com.softek.memoCraft.util.NoteTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class NoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
    }

    @Test
    public void testGetAllNotes() throws Exception {
        Note note1 = new Note("Title1", new Date(), "TextRef1", Set.of(NoteTag.PERSONAL));
        Note note2 = new Note("Title2", new Date(), "TextRef2", Set.of(NoteTag.BUSINESS, NoteTag.PERSONAL));
        List<Note> notes = Arrays.asList(note1, note2);

        when(noteService.getNotesByLabel(0, 5, Set.of(NoteTag.PERSONAL.toString())).toList()).thenReturn(notes);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].title").value("Title2"));
    }

    @Test
    public void testGetNoteById() throws Exception {
        Note note = new Note("Title", new Date(), "TextRef", Set.of(NoteTag.PERSONAL));
        when(noteService.getNoteById(1L).get()).thenReturn(note);

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void testCreateNote() throws Exception {
        Note note = new Note("Title", new Date(), "TextRef", Set.of(NoteTag.PERSONAL));
        when(noteService.saveOrUpdate(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Title\", \"label\": \"Label\", \"textRef\": \"TextRef\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    public void testUpdateNote() throws Exception {
        Note note = new Note("Title", new Date(), "TextRef", Set.of(NoteTag.PERSONAL));
        when(noteService.getNoteById(1L).get()).thenReturn(note);
        when(noteService.saveOrUpdate(any(Note.class))).thenReturn(note);

        mockMvc.perform(put("/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Title\", \"label\": \"Updated Label\", \"textRef\": \"UpdatedTextRef\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void testDeleteNote() throws Exception {
        Note note = new Note("Title", new Date(), "TextRef", Set.of(NoteTag.PERSONAL));
        when(noteService.getNoteById(1L).get()).thenReturn(note);

        mockMvc.perform(delete("/notes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetTextDocumentById() throws Exception {
        TextDocument textDocument = new TextDocument("Text");
        when(noteService.getTextDocumentById("TextRef")).thenReturn(textDocument);

        mockMvc.perform(get("/notes/text/TextRef"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Text"));
    }

    @Test
    public void testCreateTextDocument() throws Exception {
        TextDocument textDocument = new TextDocument("Text");
        when(noteService.createOrUpdateTextDocument(any(TextDocument.class))).thenReturn(textDocument);

        mockMvc.perform(post("/notes/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Text"));
    }

    @Test
    public void testUpdateTextDocument() throws Exception {
        TextDocument textDocument = new TextDocument("Updated Text");
        when(noteService.getTextDocumentById("TextRef")).thenReturn(textDocument);
        when(noteService.createOrUpdateTextDocument(any(TextDocument.class))).thenReturn(textDocument);

        mockMvc.perform(put("/notes/text/TextRef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\": \"Updated Text\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated Text"));
    }

    @Test
    public void testDeleteTextDocument() throws Exception {
        TextDocument textDocument = new TextDocument("Text");
        when(noteService.getTextDocumentById("TextRef")).thenReturn(textDocument);

        mockMvc.perform(delete("/notes/text/TextRef"))
                .andExpect(status().isNoContent());
    }
}
