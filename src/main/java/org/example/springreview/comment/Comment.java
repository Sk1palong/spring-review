package org.example.springreview.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springreview.Post.Post;
import org.example.springreview.Timestamped;
import org.example.springreview.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    private String contents;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Comment(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */

    public void updateComment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
