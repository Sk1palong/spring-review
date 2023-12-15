package org.example.springreview.Post;

import lombok.RequiredArgsConstructor;
import org.example.springreview.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto responseDto = postService.createPost(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(responseDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getPostList(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt,desc") List<String> sort
    ) {
        Page<PostResponseDto> postList = postService.getPostList(page - 1, size, sort);

        return ResponseEntity.status(HttpStatus.OK.value()).body(postList);
    }
}


