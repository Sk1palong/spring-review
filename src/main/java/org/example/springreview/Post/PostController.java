package org.example.springreview.Post;

import lombok.RequiredArgsConstructor;
import org.example.springreview.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("username = " + userDetails.getUsername());
        PostResponseDto responseDto = postService.createPost(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostList() {
        List<PostResponseDto> postList = postService.getPostList();

        return ResponseEntity.status(HttpStatus.OK.value()).body(postList);
    }
}


