package kg.uturdukeev.videoapp.repository;

import kg.uturdukeev.videoapp.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
