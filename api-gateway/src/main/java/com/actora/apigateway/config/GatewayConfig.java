package com.actora.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Global gateway configuration for logging, correlation IDs, and common filters.
 */
@Configuration
public class GatewayConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String REQUEST_START_TIME = "requestStartTime";

    /**
     * Global filter to add correlation ID to all requests
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter correlationIdFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String correlationId = request.getHeaders().getFirst(CORRELATION_ID_HEADER);

            if (correlationId == null || correlationId.isEmpty()) {
                correlationId = UUID.randomUUID().toString();
            }

            final String finalCorrelationId = correlationId;

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header(CORRELATION_ID_HEADER, finalCorrelationId)
                    .build();

            return chain.filter(exchange.mutate()
                    .request(modifiedRequest)
                    .build())
                    .then(Mono.fromRunnable(() -> {
                        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, finalCorrelationId);
                    }));
        };
    }

    /**
     * Global filter for request/response logging
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public GlobalFilter loggingFilter() {
        return (exchange, chain) -> {
            long startTime = System.currentTimeMillis();
            exchange.getAttributes().put(REQUEST_START_TIME, startTime);

            ServerHttpRequest request = exchange.getRequest();
            log.info("Incoming request: {} {} from {}",
                    request.getMethod(),
                    request.getURI().getPath(),
                    request.getRemoteAddress());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Long requestStartTime = exchange.getAttribute(REQUEST_START_TIME);
                if (requestStartTime != null) {
                    long duration = System.currentTimeMillis() - requestStartTime;
                    log.info("Outgoing response: {} {} - Status: {} - Duration: {}ms",
                            request.getMethod(),
                            request.getURI().getPath(),
                            exchange.getResponse().getStatusCode(),
                            duration);
                }
            }));
        };
    }
}
