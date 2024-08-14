package kg.uturdukeev.videoapp.controller;

import kg.uturdukeev.videoapp.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("videos", videoService.getAllVideos());
        return "index";
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

    @GetMapping("/video/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> streamVideo(@PathVariable String fileName) {
        try {
            Resource file = videoService.getVideoFile(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
