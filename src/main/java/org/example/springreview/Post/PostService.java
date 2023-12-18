package org.example.springreview.Post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springreview.exception.CustomException;
import org.example.springreview.exception.ErrorCode;
import org.example.springreview.image.Image;
import org.example.springreview.image.ImageRepository;
import org.example.springreview.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, MultipartFile[] files, User user) throws IOException {
        Post post = new Post(requestDto);

        post.setUser(user);

        Post savePost = postRepository.save(post);

        if (!Objects.isNull(files)) {
            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();

                Long size = file.getSize();

                String contentType = file.getContentType();

                String fullPath = "/Users/Home/Desktop/Coding/prac/spring-review/src/main/resources/static/image/" + originalFilename;

                Image image = new Image(originalFilename, originalFilename, fullPath, size, contentType);
                image.setPost(post);
                post.createImage(image);
                imageRepository.save(image);

                file.transferTo(new File(fullPath));
            }
        }

        return new PostResponseDto(savePost);
    }

    public Page<PostResponseDto> getPostList(int page, int size, List<String> sort) {

        List<Sort.Order> orders = sort.stream()
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(
                            "asc".equalsIgnoreCase(parts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC,
                            parts[0]
                    );
                })
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        Page<Post> postList = postRepository.findAll(pageable);

        return postList.map(PostResponseDto::new);
    }

    public PostResponseDto getPost(Long postId) {
        Post post = findPostById(postId);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, User user) {
        Post post = findPostById(postId);

        if(!user.getId().equals(post.getUser().getId())){
            throw new CustomException(ErrorCode.USER_NOT_MATCHES);
        }

        post.updatePost(requestDto);

        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPostById(postId);

        if (!user.getId().equals(post.getUser().getId())) {
            throw new CustomException(ErrorCode.USER_NOT_MATCHES);
        }

        postRepository.delete(post);
    }

    public Post findPostById(Long postId) {

        return postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteOldPosts() {

        LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);

        List<Post> oldPosts = postRepository.findAllByModifiedAtBefore(ninetyDaysAgo);

        postRepository.deleteAll(oldPosts);
    }
}
