package org.example.springreview.Post;

import lombok.RequiredArgsConstructor;
import org.example.springreview.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto);

        post.setUser(user);

        Post savePost = postRepository.save(post);

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
}
