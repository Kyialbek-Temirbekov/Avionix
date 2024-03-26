package avia.cloud.gatewayserver.filter;

import lombok.SneakyThrows;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

@Component
public class AddRequestOriginalPathFilter implements GlobalFilter {
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrlAttribute = Objects.requireNonNull(exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)).toString();
        String originalRequestUrl = Arrays.stream(requestUrlAttribute.substring(1).split(",")).findFirst().get();
        String originalRequestPath = new URI(originalRequestUrl).getPath();
        exchange.getRequest().mutate().header("Original-Path", originalRequestPath).build();
        exchange.getRequest().mutate().header("Original-Url", originalRequestUrl).build();
        return chain.filter(exchange);
    }
}
