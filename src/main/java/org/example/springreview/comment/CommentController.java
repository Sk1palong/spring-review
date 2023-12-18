package org.example.springreview.comment;

import lombok.RequiredArgsConstructor;
import org.example.springreview.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(commentService.createComment(postId, requestDto, userDetails.getUser()));
    }
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getComments(
            @PathVariable Long postId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") List<String> sort) {

        Page<CommentResponseDto> comments = commentService.getComments(postId, page-1, size, sort);

        return ResponseEntity.status(HttpStatus.OK.value()).body(comments);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(HttpStatus.OK.value()).body(commentService.updateComment(postId, commentId, requestDto, userDetails.getUser()));

    }
}
