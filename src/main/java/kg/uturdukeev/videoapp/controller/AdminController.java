package kg.uturdukeev.videoapp.controller;

import kg.uturdukeev.videoapp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final VideoService videoService;

    @GetMapping("/")
    public String index() {
        return "admin";
    }

    @PostMapping("/upload")
    public String uploadVideo(@RequestParam("file") MultipartFile file,
                              @RequestParam("title") String title,
                              RedirectAttributes redirectAttributes) {
        try {
            videoService.saveVideo(file, title);
            redirectAttributes.addFlashAttribute("message", "Video uploaded successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An error occurred during file upload.");
        }
        return "redirect:/";
    }
}
