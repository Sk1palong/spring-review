package org.example.springreview.comment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springreview.post.Post;
import org.example.springreview.post.PostRepository;
import org.example.springreview.exception.CustomException;
import org.example.springreview.exception.ErrorCode;
import org.example.springreview.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<CommentResponseDto> getComments(Long postId, int page, int size, List<String> sort) {
        findPostById(postId);

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

        Page<Comment> comments = commentRepository.findAll(pageable);

        return comments.map(CommentResponseDto::new);
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
        findPostById(postId);

        Comment comment = findCommentById(commentId);

        isSameUser(user, comment);

        comment.updateComment(requestDto);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, User user) {
        findPostById(postId);

        Comment comment = findCommentById(commentId);

        isSameUser(user, comment);

        commentRepository.delete(comment);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteOldComments() {

        LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);

        List<Comment> oldComments = commentRepository.findAllByModifiedAtBefore(ninetyDaysAgo);

        commentRepository.deleteAll(oldComments);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND)
        );
    }

    public void isSameUser(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_MATCHES);
        }
    }
}
