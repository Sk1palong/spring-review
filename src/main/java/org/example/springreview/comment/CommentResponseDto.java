package org.example.springreview.comment;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.username = comment.getUser().getUsername();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

    public static List<CommentResponseDto> mapToDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
