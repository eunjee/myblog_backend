package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {

    @Id
    @Column(name="resume_id")
    private Long id;

    private String name;

    private String url;

    private int size;

    @Builder
    private Resume(String name, String url, int size){
        this.name = name;
        this.url = url;
        this.size = size;
    }
}
