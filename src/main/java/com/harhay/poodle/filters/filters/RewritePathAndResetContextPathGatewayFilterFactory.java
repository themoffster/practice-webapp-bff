package com.harhay.poodle.filters.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
class RewritePathAndResetContextPathGatewayFilterFactory extends AbstractGatewayFilterFactory<RewritePathGatewayFilterFactory.Config> {

    private final RewritePathGatewayFilterFactory delegate;

    public RewritePathAndResetContextPathGatewayFilterFactory() {
        super(RewritePathGatewayFilterFactory.Config.class);
        this.delegate = new RewritePathGatewayFilterFactory();
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return delegate.shortcutFieldOrder();
    }

    @Override
    public GatewayFilter apply(RewritePathGatewayFilterFactory.Config config) {
        GatewayFilter delegateFilter = delegate.apply(config);
        return (exchange, chain) -> {
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().contextPath("/").build();
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            return delegateFilter.filter(mutatedExchange, chain);
        };
    }
}
