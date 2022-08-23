package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {

    private final UserService userService;
    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;


    @GetMapping
    public String homeView(Authentication authentication,
                           @ModelAttribute("noteForm") Note note,
                           @ModelAttribute("fileForm") File file,
                           @ModelAttribute("credentialForm") Credential credential,
                           Model model) {
        User user = userService.getUser(authentication.getName());
        if(user == null) {
            return "redirect:/home";
        }

        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", credentialService.getCredentialsListByUserId(user.getUserid()));
        model.addAttribute("notes", noteService.getNoteListByUserId(user.getUserid()));
        model.addAttribute("files", fileService.getFilesFromUserId(user.getUserid()));

        return "home";
    }

}
