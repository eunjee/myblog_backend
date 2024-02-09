package jp.falsystack.falsylog_backend.service;

import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Resume;
import jp.falsystack.falsylog_backend.exception.MemberNotFound;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.ResumeRepository;
import jp.falsystack.falsylog_backend.request.ResumeCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public boolean saveResume(String userName, ResumeCreate resumeCreate, String fileUrl) {
        Resume resume = Resume.builder()
                .name(resumeCreate.getMultipartFile().getOriginalFilename())
                .url(fileUrl)
                .size(resumeCreate.getMultipartFile().getSize())
                .build();

        Member member = memberRepository.findByName(userName).orElseThrow(MemberNotFound::new);

        member.addResume(resume);

        //memberRepository.save(member);

        return true;
    }

    public String getResume(String userName){
        Member member = memberRepository.findByName(userName).orElseThrow(MemberNotFound::new);
        if(member.getResume()==null){
            return "";
        }
        return member.getResume().getUrl();
    }
    @Transactional
    public boolean deleteResume(String userName) {
        Member member = memberRepository.findByName(userName).orElseThrow(MemberNotFound::new);
        //이력서 삭제
        resumeRepository.delete(member.getResume());
        //연관관계 끊기
        member.clearResume();

        return true;
    }
}
