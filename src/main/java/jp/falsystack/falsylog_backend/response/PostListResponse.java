package jp.falsystack.falsylog_backend.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponse {
    private final Long totalLength;
    private final Integer lastPage;
    private final Boolean isLast;
    private final List<PostResponse> postResponses;

    @Builder
    public PostListResponse(Long totalLength,
                            Integer lastPage,
                            boolean isLast,
                            List<PostResponse> postResponses) {
        this.totalLength = totalLength;
        this.lastPage = lastPage;
        this.isLast = isLast;
        this.postResponses = postResponses;
    }
}
