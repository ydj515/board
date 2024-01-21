package kr.co.promptech.noticeboard.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.promptech.noticeboard.BaseTest;
import kr.co.promptech.noticeboard.RestdocsConfig;
import kr.co.promptech.noticeboard.domain.global.request.JoinRequest;
import kr.co.promptech.noticeboard.enums.ResultCode;
import kr.co.promptech.noticeboard.service.member.MemberAuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestdocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberAuthControllerTest extends BaseTest {

    private final static String PREFIX = "/api/auth";

    @Autowired
    protected MockMvc mock;

    @Autowired
    protected RestDocumentationResultHandler docs;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MemberAuthService memberAuthService;

    @Test
    @DisplayName(PREFIX + "/join")
    void join() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("juno@auth.com", "password", "name", "nick", "19941030");

        //when
        ResultActions resultActions = mock.perform(
                post(PREFIX + "/join").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
        ).andDo(print());

        //then
        String contentAsString = resultActions.andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertThat(contentAsString).contains(ResultCode.JOIN_SUCCESS.code);

        resultActions.andDo(docs.document(
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("birth").type(JsonFieldType.STRING).description("생년월일 ex) 19941030")
                                .optional()
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일")
                )
        ));
    }

    @Test
    void login() {
    }

    @Test
    void refresh() {
    }
}