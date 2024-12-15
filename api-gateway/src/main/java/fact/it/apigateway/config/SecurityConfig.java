package fact.it.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .authorizeExchange(exchange ->
                        exchange.pathMatchers(HttpMethod.GET, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .permitAll()  // Allow GET requests for all resources without token
                                .pathMatchers(HttpMethod.POST, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .permitAll()  // Allow POST requests for all resources without token
                                .pathMatchers(HttpMethod.PUT, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .permitAll()  // Allow PUT requests for all resources without token
                                .pathMatchers(HttpMethod.DELETE, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .authenticated()  // Require token for DELETE requests
                                .anyExchange()
                                .authenticated()  // Default: Authenticate all other requests
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())
                );
        return serverHttpSecurity.build();
    }

}