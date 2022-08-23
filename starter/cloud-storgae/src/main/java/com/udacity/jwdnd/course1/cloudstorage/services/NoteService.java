package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note getNote(String notetitle, String description) {
        return noteMapper.getNote(notetitle, description);
    }

    public Note getNoteById(Integer noteid) {
        return noteMapper.getNoteById(noteid);
    }

    public List<Note> getNoteListByUserId(Integer userid) {
        return noteMapper.getUserNotes(userid);
    }

    public void save(Note note, Integer userid) {
        note.setUserid(userid);
        noteMapper.save(note);
    }

    public void update(Integer noteid, String title, String description, Integer userid) {
        noteMapper.update(noteid, title, description, userid);
    }

    public void delete(Integer noteid, Integer userid) {
        noteMapper.delete(noteid, userid);
    }

}
