package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ControllerAdvice
public class FileController {

    private final FileService fileService;
    private final UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping(value = "download/files/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] getFile(@PathVariable String filename) {
        File file = fileService.getFileByName(filename);
        return file.getFiledata();
    }

    @GetMapping("/delete/files/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userid = userService.getUser(authentication.getName()).getUserid();
        fileService.delete(fileId,userid);
        redirectAttributes.addFlashAttribute("tab", "nav-files-tab");
        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/home";
    }

    @PostMapping("/upload/files")
    public String uploadFiles(Authentication authentication,
                              @RequestParam("fileUpload") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes) {

        Integer userid = userService.getUser(authentication.getName()).getUserid();
        if(fileService.isExist(multipartFile.getOriginalFilename())) {
            redirectAttributes.addFlashAttribute("error", "File already exists!");
            return "redirect:/home";
        }

        if(multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "File cannot be empty");
            return "redirect:/home";
        }

        try {
            fileService.save(multipartFile, userid);
            redirectAttributes.addFlashAttribute("result", "success");
            Thread.sleep(300);
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("result", "notSaved");
        }
        return "redirect:/home";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String fileToBig(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "You can only upload files which ar smaller than 2MB!");
        return "redirect:/home";
    }

}
