package org.example.springreview.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.springreview.post.Post;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    private String uploadFilename;

    private String storedFilename;

    private String fullPath;

    private Long size;

    private String extension;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Image(String uploadFilename, String storedFilename, String fullPath, Long size, String extension) {
        this.uploadFilename = uploadFilename;
        this.storedFilename = storedFilename;
        this.fullPath = fullPath;
        this.size = size;
        this.extension = extension;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void setPost(Post post) {
        this.post = post;
        if (!post.getImages().contains(this)) {
            post.getImages().add(this);
        }
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */


}
