package org.example.springreview.comment;

import lombok.RequiredArgsConstructor;
import org.example.springreview.Post.Post;
import org.example.springreview.Post.PostRepository;
import org.example.springreview.exception.CustomException;
import org.example.springreview.exception.ErrorCode;
import org.example.springreview.user.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = findPostById(postId);

        Comment comment = new Comment(requestDto);

        comment.setPost(post);
        comment.setUser(user);

        Comment saveComment = commentRepository.save(comment);

        return new CommentResponseDto(saveComment);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
    }
}
