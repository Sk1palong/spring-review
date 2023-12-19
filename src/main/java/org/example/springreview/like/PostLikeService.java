package org.example.springreview.like;

import lombok.RequiredArgsConstructor;
import org.example.springreview.post.Post;
import org.example.springreview.post.PostRepository;
import org.example.springreview.exception.CustomException;
import org.example.springreview.exception.ErrorCode;
import org.example.springreview.user.User;
import org.example.springreview.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );

        if (Objects.equals(user.getId(), post.getUser().getId())) {
            throw new CustomException(ErrorCode.SELF_LIKE_ERROR);
        }
        PostLike postLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();
        if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_LIKE);
        }

        postLikeRepository.save(postLike);
    }
}
