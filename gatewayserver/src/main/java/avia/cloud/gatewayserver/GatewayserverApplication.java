package avia.cloud.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator cloudTicketRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/avionix/flight/**")
						.filters(f -> f.rewritePath("/avionix/flight/(?<segment>.*)","/${segment}"))
						.uri("lb://FLIGHT"))
				.route(p -> p
						.path("/avionix/client/**")
						.filters(f -> f.rewritePath("/avionix/client/(?<segment>.*)","/${segment}"))
						.uri("lb://CLIENT"))
				.route(p -> p
						.path("/avionix/discovery/**")
						.filters(f -> f.rewritePath("/avionix/discovery/(?<segment>.*)","/${segment}"))
						.uri("lb://INFO")).build();
	}

}
