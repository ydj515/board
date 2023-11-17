package kr.co.promptech.noticeboard.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class WebClientUtil {

    private final WebClient webClient;

    public WebClientUtil(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
//                .baseUrl("")
                .filter(headerSettingFilter())
                .build();
    }

    private ExchangeFilterFunction headerSettingFilter() {
        return (request, next) -> next.exchange(ClientRequest.from(request)
                .headers(headers -> {
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    headers.add(HttpHeaders.ACCEPT, "application/json");
//                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer yourAccessToken");
                })
                .build());
    }

    public <T> T get(String path, Class<T> responseType, Map<String, String> params) {
        WebClient.RequestHeadersUriSpec<?> request = (WebClient.RequestHeadersUriSpec<?>) webClient.get().uri(path, params);

        return request.retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <T> T post(String path, Object requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(path)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <T> T patch(String path, Object requestBody, Class<T> responseType) {
        return webClient.patch()
                .uri(path)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    public <T> T put(String path, Object requestBody, Class<T> responseType) {
        return webClient.put()
                .uri(path)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
    
    public HttpStatus delete(String path) {
        return webClient.delete()
                .uri(path)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(HttpStatus.OK);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                })
                .block();
    }
}
