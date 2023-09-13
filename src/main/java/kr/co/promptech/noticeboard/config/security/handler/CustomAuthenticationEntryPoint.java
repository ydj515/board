package kr.co.promptech.noticeboard.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.promptech.noticeboard.constants.Constants;
import kr.co.promptech.noticeboard.domain.global.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        sendErrorDetails(request, response);
    }

    private void sendErrorDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<ErrorDto> errors = new ArrayList<>();

        errors.add(createErrorDto(request));

        ProblemDetail pb = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Authentication Failed");
        pb.setType(URI.create(Constants.SWAGGER_URL));
        pb.setProperty("errors", errors);
        pb.setInstance(URI.create(request.getRequestURI()));

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(pb));
    }

    private ErrorDto createErrorDto(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String location = requestURI.contains("/login") ? "email / password" : "access token";
        String details = requestURI.contains("/login") ? "please check email or password" : "please check access token";

        return ErrorDto.builder()
                .location(location)
                .details(details)
                .build();
    }
}