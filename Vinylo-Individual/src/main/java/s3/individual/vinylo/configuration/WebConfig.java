package s3.individual.vinylo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import s3.individual.vinylo.configuration.security.auth.AuthenticationRequestFilter;

@EnableWebSecurity
// Enables method-level security annotations (e.g., @RolesAllowed)
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                        AuthenticationEntryPoint authenticationEntryPoint,
                        AuthenticationRequestFilter authenticationRequestFilter) throws Exception {
                httpSecurity
                                // Disables CSRF (Cross-Site Request Forgery) protection, since this is
                                // typically handled by tokens in stateless APIs
                                .csrf(AbstractHttpConfigurer::disable)
                                // Disables default form-based login because I use token login
                                .formLogin(AbstractHttpConfigurer::disable)
                                // Configures session management to use stateless sessions (i.e., no server-side
                                // sessions)
                                .sessionManagement(configurer -> configurer
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                // Configures authorization rules for incoming HTTP requests
                                .authorizeHttpRequests(registry -> registry
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/users", "/login", "/vinyls",
                                                                "/vinyls/{id}",
                                                                "/spotify-embed", "/collections",
                                                                "/collections/{id}",
                                                                "/collections/{userId}",
                                                                "/collections/{userId}/{vinylId}")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/users", "/users/{id}", "/artist",
                                                                "/vinyls", "/vinyls/{id}",
                                                                "/spotify-embed", "/collections", "/collections/{id}",
                                                                "/auctions", "/collections/user/{userId}",
                                                                "/collections/userVinyl/{userId}/{vinylId}")
                                                .permitAll()
                                                // Require authentication for any other request
                                                .anyRequest().authenticated())
                                .exceptionHandling(configure -> configure
                                                .authenticationEntryPoint(authenticationEntryPoint))
                                .addFilterBefore(authenticationRequestFilter,
                                                UsernamePasswordAuthenticationFilter.class);
                return httpSecurity.build();
        }

        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(@NonNull CorsRegistry registry) {
                                // This allows CORS for all paths
                                registry.addMapping("/**")
                                                .allowedOrigins("http://localhost:5173")
                                                .allowedMethods("GET", "POST", "PUT", "DELETE")
                                                // This allows all headers to request.
                                                .allowedHeaders("*")
                                                // This allows credentials like for example cookies
                                                .allowCredentials(true);
                        }
                };
        }
}