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
                        // Allow GET requests for all resources without token
                        exchange.pathMatchers(HttpMethod.GET, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .permitAll()

                                // Require authentication for POST, PUT, and DELETE requests
                                .pathMatchers(HttpMethod.POST, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .authenticated()  // Requires token for POST

                                .pathMatchers(HttpMethod.PUT, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .authenticated()  // Requires token for PUT

                                .pathMatchers(HttpMethod.DELETE, "/user/**", "/workout/**", "/record/**", "/health/**")
                                .authenticated()  // Requires token for DELETE

                                // Default: Authenticate all other requests
                                .anyExchange()
                                .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())  // JWT authentication for token validation
                );
        return serverHttpSecurity.build();
    }
}
