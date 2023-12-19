package org.example.springreview.post;

import lombok.Getter;
import org.example.springreview.comment.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public class PostResponseDto {
    private String title;
    private String username;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
        if (Objects.nonNull(post.getComments())) {
            this.comments = CommentResponseDto.mapToDtoList(post.getComments());
        }
    }
}
