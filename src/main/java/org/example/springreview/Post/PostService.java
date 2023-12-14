package org.example.springreview.Post;

import lombok.RequiredArgsConstructor;
import org.example.springreview.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto);

        post.setUser(user);
        System.out.println(post.getContents());
        System.out.println(post.getUser().getUsername());
        Post savePost = postRepository.save(post);

        return new PostResponseDto(savePost);

    }

    public List<PostResponseDto> getPostList() {
        List <PostResponseDto> postList = postRepository.findAll().stream().map(PostResponseDto::new).toList();

        return postList;
    }
}
