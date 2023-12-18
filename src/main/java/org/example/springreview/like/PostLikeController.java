package org.example.springreview.like;


import lombok.RequiredArgsConstructor;
import org.example.springreview.CommonResponseDto;
import org.example.springreview.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/likes")
    public ResponseEntity<CommonResponseDto> createLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.createLike(postId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CommonResponseDto("좋아요를 누르셨습니다", HttpStatus.CREATED.value()));
    }
}
