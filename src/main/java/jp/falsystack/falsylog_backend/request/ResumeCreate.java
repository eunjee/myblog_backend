package jp.falsystack.falsylog_backend.request;

import jakarta.validation.constraints.NotNull;
import jp.falsystack.falsylog_backend.domain.Resume;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter //form-data DTO의 경우 생성자는 없어도 되지만 setter 필요
public class ResumeCreate {
    @NotNull
    private MultipartFile multipartFile;

}
