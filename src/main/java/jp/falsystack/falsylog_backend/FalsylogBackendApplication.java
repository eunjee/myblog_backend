package jp.falsystack.falsylog_backend;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.PostHashTagRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableConfigurationProperties(AppConfig.class)
@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
public class FalsylogBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(FalsylogBackendApplication.class, args);
  }

  // TODO: クライアントからな要求でログインページが完了するまでは認証処理させない。
  @Bean
  @Profile("local")
  public TestDataInit testDataInit(MemberRepository memberRepository,
                                   PostRepository postRepository, PostHashTagRepository postHashTagRepository,HashTagRepository hashTagRepository) {

    return new TestDataInit(memberRepository, postRepository,postHashTagRepository,hashTagRepository);
  }

  @RequiredArgsConstructor
  static class TestDataInit {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final HashTagRepository hashTagRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
      var member = Member.of("test-user",
          "test@test.com",
          "1q2w3e4r",
          LocalDateTime.now());

      var hashTag1 = HashTag.builder()
              .name("#AWS")
              .build();

      var hashTag2 = HashTag.builder()
              .name("#Git")
              .build();

      // TODO: Reactでの無限スクロールのテストの為のデータを入れておく
      var list = new ArrayList<Post>();
      var postHashTagList = new ArrayList<PostHashTag>();
      for (int i = 0; i < 100; i++) {

        var post = Post.builder().title("테스트용 제목입니다. " + i).content(
                "테스트용 내용입니다. 반적으로 새로운 소프트웨어 개발팀이 구성되면 팀이 코드를 정확히 어떻게 작성할 것인지에 대한 논의가 이루어집니다. 프로그래밍 언어나 주요 프레임워크의 메인테이너가 이에 대해 의견을 가지고 있다면 이러한 논의의 기본적인 출발점을 제공하는 데 도움이 될 수 있습니다.\n"
                    + "\n"
                    + "리액트는 어떻게 코드를 써야하는 지에 대한 주장이 강하지 않은 것으로 알려져 있지만, 최신 리액트 문서에서는 어떻게 리액트 코드를 작성해야하는 지에 대해 꽤 많은 권장 사항을 제시하고 있습니다. 이 문서가 베타 버전으로 처음 출시된 이후, 다른 개발자들과 저는 팀에서 코드 스타일에 대해 대화할 때 이 문서를 기준으로서 점점 더 많이 참조해 왔습니다.\n"
                    + "\n"
                    + "이 글에서는 리액트 코드 스타일에 대한 팀 토론에서 가장 자주 언급되는 리액트 문서의 권장사항을 이야기 합니다. 각각에 대해 간단한 요약과 코드 스니펫을 작성할 것입니다. 각 글에 포함된 링크를 따라 리액트 문서의 관련된 부분으로 이동하면 훨씬 더 많은 정보와 근거를 확인하실 수 있습니다.\n"
                    + "\n"
                    + "이러한 권장 사항 중 일부는 중요하지 않은 코드 스타일에 대한 의견처럼 느껴질 수도 있습니다. 하지만 리액트 문서에서 설명하는 것처럼 권장 사항을 따르지 않는 것은 비용이나 위험이 따릅니다. 여러분은 자유롭게 결정할 수 있지만, 리액트 코어팀은 여러분이나 저보다 훨씬 더 많은 것을 생각했을 것임을 명심하세요. 물론 이것이 항상 옳다는 것을 의미하지는 않지만, 적어도 그들의 의견을 들어보는 것은 그만한 가치가 있을 것입니다.\n"
                    + "\n")
            .build();

        //해시태그 추가
        PostHashTag postHashTag1 = PostHashTag.builder()
                .post(post)
                .hashTag(hashTag1)
                .build();
        PostHashTag postHashTag2 = PostHashTag.builder()
                .post(post)
                .hashTag(hashTag2)
                .build();
        postHashTagList.add(postHashTag1);
        postHashTagList.add(postHashTag2);


        post.addMember(member);
        post.addPostHashTags(Arrays.asList(postHashTag1,postHashTag2));
        list.add(post);
      }
      postRepository.saveAll(list);
      postHashTagRepository.saveAll(postHashTagList);
      memberRepository.save(member);
      hashTagRepository.save(hashTag1);
      hashTagRepository.save(hashTag2);

    }
  }


}
