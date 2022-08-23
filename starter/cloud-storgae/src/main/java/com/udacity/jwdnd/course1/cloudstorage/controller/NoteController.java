package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class NoteController {

    private final UserService userService;
    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    @PostMapping("/notes")
    public String noteUpdateAndCreateView(Authentication authentication,
                                          @ModelAttribute("noteForm") Note note,
                                          @ModelAttribute("credentialForm") Credential credential,
                                          Model model) {
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        Note existingNote = noteService.getNoteById(note.getNoteid());
        Note duplicatedNote = noteService.getNote(note.getNotetitle(), note.getNotedescription());
        if(note.getNotedescription().length() > 1000) {
            model.addAttribute("error", "Description can't be greater than 1000 chars!");
        } else if(duplicatedNote != null) {
            model.addAttribute("error", "This note entry already exists!");
        } else if(existingNote == null) {
            noteService.save(note, userid);
            model.addAttribute("result", "success");
        } else {
            noteService.update(existingNote.getNoteid(), note.getNotetitle(), note.getNotedescription(), userid);
            model.addAttribute("result", "success");
        }

        model.addAttribute("tab", "nav-notes-tab");
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("files", fileService.getFilesFromUserId(userid));
        model.addAttribute("credentials", credentialService.getCredentialsListByUserId(userid));
        model.addAttribute("notes", noteService.getNoteListByUserId(userid));
        return "redirect:/home";
    }

    @GetMapping("/delete/notes/{noteid}")
    public String deleteNote(@PathVariable Integer noteid, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        noteService.delete(noteid, userid);
        redirectAttributes.addFlashAttribute("tab", "nav-notes-tab");
        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/home";
    }
}
