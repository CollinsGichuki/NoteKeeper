package com.example.android.notekeeper;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    @Test
    public void createNewNote() {
        DataManager dm = DataManager.getInstance();
        final CourseInfo course = dm.getCourse("android_async");
        final String noteTitle = "Test Note Title";
        final String noteText = "This is the test Note Text";

        int noteIndex = dm.createNewNote();
        //Getting the new note
        NoteInfo newNote = dm.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNotes = dm.getNotes().get(noteIndex);
        //Checking whether the new note is the same
        assertEquals(newNote.getCourse(), compareNotes.getCourse());
        assertEquals(newNote.getTitle(), compareNotes.getTitle());
        assertEquals(newNote.getText(),compareNotes.getText());
    }
}