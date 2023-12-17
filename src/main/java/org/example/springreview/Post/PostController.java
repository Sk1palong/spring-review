package org.example.springreview.Post;

import lombok.RequiredArgsConstructor;
import org.example.springreview.CommonResponseDto;
import org.example.springreview.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE} )
    public ResponseEntity<PostResponseDto> createPost(@RequestPart PostRequestDto requestDto, @RequestPart("files")MultipartFile[] files, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        PostResponseDto responseDto = postService.createPost(requestDto, files, userDetails.getUser());

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

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {

        return ResponseEntity.status((HttpStatus.OK)).body(postService.getPost(postId));
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postId, requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK.value()).body(new CommonResponseDto(postId + "번 게시글이 삭제되었습니다.", HttpStatus.OK.value()));
    }
}


