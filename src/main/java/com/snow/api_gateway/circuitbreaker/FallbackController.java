package com.snow.api_gateway.circuitbreaker;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback")
    public Mono<ResponseEntity<String>> fallback(ServerWebExchange exchange) {
        // If response already committed (e.g. Netty timed out and wrote a status), avoid writing again
        if (exchange.getResponse().isCommitted()) {
            return Mono.empty();
        }

        return Mono.just(
                ResponseEntity
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Backend is temporarily unavailable. Please retry later.")
        );
    }
}
