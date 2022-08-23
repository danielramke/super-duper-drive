package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditor;
import java.util.Objects;

@Controller
public class CredentialController {

    @Value("${mySecretKey}")
    private String key;

    private final UserService userService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final NoteService noteService;
    private final FileService fileService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService, NoteService noteService, FileService fileService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.noteService = noteService;
        this.fileService = fileService;
    }

    @PostMapping("/credentials")
    public String credentialUpdateAndCreateView(Authentication authentication,
                                                @ModelAttribute("credentialForm") Credential credential,
                                                @ModelAttribute("noteForm") Note note,
                                                Model model) {
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        Credential existingCredential = credentialService.getById(credential.getCredentialid());
        Credential duplicatedCredential = credentialService.getByUrlAndUsername(credential.getUrl(), credential.getUsername());

        if(existingCredential == null) {
            try {
                if(duplicatedCredential != null) {
                    if(Objects.equals(duplicatedCredential.getUsername(), credential.getUsername())) {
                        model.addAttribute("error", "This username already exists!");
                    }
                } else {
                    PropertyEditor editor = new URLEditor();
                    editor.setAsText(credential.getUrl());
                    credentialService.save(credential, userid);
                    model.addAttribute("result", "success");
                }
            } catch (IllegalArgumentException exception) {
                model.addAttribute("error", "Invalid URL");
            }
        } else {
            String updatedPassword = encryptionService.encryptValue(credential.getPassword(), key);
            credentialService.update(existingCredential.getCredentialid(), credential.getUrl(), credential.getUsername(), updatedPassword, userid);
            model.addAttribute("result", "success");
        }

        model.addAttribute("tab", "nav-credentials-tab");
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", credentialService.getCredentialsListByUserId(userid));
        model.addAttribute("notes", noteService.getNoteListByUserId(userid));
        model.addAttribute("files", fileService.getFilesFromUserId(userid));
        return "redirect:/home";
    }

    @GetMapping("/delete/credentials/{credentialid}")
    public String deleteCredential(@PathVariable Integer credentialid, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        credentialService.delete(credentialid, userid);
        redirectAttributes.addFlashAttribute("tab", "nav-credentials-tab");
        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/home";
    }

}
