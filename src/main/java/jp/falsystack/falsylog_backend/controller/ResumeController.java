package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.exception.FileUploadFail;
import jp.falsystack.falsylog_backend.request.ResumeCreate;
import jp.falsystack.falsylog_backend.service.ResumeService;
import jp.falsystack.falsylog_backend.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final S3UploadService s3UploadService;

    //TODO 확장자, 파일 크기 제한
    @PostMapping("member/{name}/resume")
    public String createResume(@PathVariable String name, @ModelAttribute ResumeCreate resumeCreate){
        //실제 파일 업로드
        String fileUrl="";
        try {
            fileUrl=s3UploadService.saveFile(resumeCreate.getMultipartFile());
        }catch(Exception e){
            throw new FileUploadFail();
        }
        //DB에 적재하기
        resumeService.saveResume(name,resumeCreate,fileUrl);

        return "success";
    }

    @GetMapping("member/{name}/resume")
    public String getResume(@PathVariable String name){
        return resumeService.getResume(name);
    }

    @DeleteMapping("member/{name}/resume")
    public String deleteResume(@PathVariable String name){
        resumeService.deleteResume(name);
        return "success";
    }
}
