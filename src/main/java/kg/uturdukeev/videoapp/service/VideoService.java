package kg.uturdukeev.videoapp.service;

import kg.uturdukeev.videoapp.entity.Video;
import kg.uturdukeev.videoapp.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final VideoRepository videoRepository;
    private final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB


    public Video saveVideo(MultipartFile file, String title) throws IOException {

        if (!isValidVideoFile(file)) {
            throw new IllegalArgumentException("Invalid file type. Only video files are allowed.");
        }

        if (!isValidFileSize(file, MAX_FILE_SIZE)) {
            throw new IllegalArgumentException("File size exceeds the maximum limit of 100MB.");
        }

        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Video video = new Video();
        video.setTitle(title);
        video.setDescription("test");
        video.setFileName(fileName);

        return videoRepository.save(video);
    }

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }


    public Resource getVideoFile(String fileName) throws MalformedURLException {
        Path path = Paths.get(uploadDir, fileName);
        return new UrlResource(path.toUri());
    }

    public boolean isValidVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        List<String> allowedMimeTypes = Arrays.asList("video/mp4", "video/avi", "video/mov");

        return allowedMimeTypes.contains(contentType);
    }

    public boolean isValidFileSize(MultipartFile file, long maxSize) {
        return file.getSize() <= maxSize;
    }


}
