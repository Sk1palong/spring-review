package org.example.springreview.Post;

import lombok.Getter;
import org.example.springreview.comment.Comment;
import org.example.springreview.comment.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

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
        this.comments = CommentResponseDto.mapToDtoList(post.getComments());
    }
}
