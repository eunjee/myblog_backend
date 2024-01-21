package jp.falsystack.falsylog_backend.request.post;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearch {

    private static final int MAX_SIZE = 100;

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 5;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private String hashTags;
    @Builder.Default
    private String sort="desc";

    public long getOffset() {
        return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZE);
    }
}
