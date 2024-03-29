package kr.co.promptech.noticeboard.repository;

import kr.co.promptech.noticeboard.domain.global.request.JoinRequest;
import kr.co.promptech.noticeboard.domain.entity.Member;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Rollback(value = false)
    public void memberRepositoryTest() {
        // given
        JoinRequest joinRequest = JoinRequest.builder()
                .email("aaa@naver.com")
                .password("testpassword")
                .name("testName")
                .nickname("testNickName")
                .birth("11112233")
                .build();
        Member member = Member.of(joinRequest);
//        System.out.println(member.getId());

        // when
        Member newMember = memberRepository.save(member);

        // then
        System.out.println(newMember.getId());
    }

}