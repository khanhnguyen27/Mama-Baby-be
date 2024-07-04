package com.myweb.mamababy.configurations;


import com.myweb.mamababy.filters.JwtTokenFilter;
import com.myweb.mamababy.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig implements  WebMvcConfigurer{
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    //Pair.of(String.format("%s/products", apiPrefix), "GET"),
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                              .requestMatchers("**")
                              .permitAll()
//                            .requestMatchers(
//                                    String.format("%s/users/register", apiPrefix),
//                                    String.format("%s/users/login", apiPrefix)
//                            )
//                            .permitAll()
//
//                            .requestMatchers(GET,
//                                    String.format("%s/active/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(POST,
//                                    String.format("%s/active/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//                            .requestMatchers(GET,
//                                    String.format("%s/age", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/age/admin", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(POST,
//                                    String.format("%s/age/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(PUT,
//                                    String.format("%s/age/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//                            .requestMatchers(GET,
//                                    String.format("%s/brands", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/brands/admin", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(POST,
//                                    String.format("%s/brands/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(PUT,
//                                    String.format("%s/brands/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//                            .requestMatchers(GET,
//                                    String.format("%s/categories", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/categories/admin", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(POST,
//                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(PUT,
//                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//                            .requestMatchers(GET,
//                                    String.format("%s/comments/**", apiPrefix)).permitAll()
//                            .requestMatchers(POST,
//                                    String.format("%s/comments", apiPrefix)).hasAnyRole(Role.MEMBER)
//                            .requestMatchers(PUT,
//                                    String.format("%s/comments/{id}", apiPrefix)).hasAnyRole(Role.MEMBER)
//                            .requestMatchers(PUT,
//                                    String.format("%s/comments/status/**", apiPrefix)).hasAnyRole(Role.STAFF)
//
//                            .requestMatchers(GET,
//                                    String.format("%s/article", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/article/no_page", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/article/{id}}", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/article/store", apiPrefix)).hasAnyRole(Role.STAFF)
//                            .requestMatchers(POST,
//                                    String.format("%s/article", apiPrefix)).hasAnyRole(Role.STAFF)
//                            .requestMatchers(PUT,
//                                    String.format("%s/article/**", apiPrefix)).hasAnyRole(Role.STAFF)
//
//                            .requestMatchers(GET,
//                                    String.format("%s/users/details", apiPrefix)).permitAll()
//                            .requestMatchers(GET,
//                                    String.format("%s/users/all", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(GET,
//                                    String.format("%s/users/findByYear", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(PUT,
//                                    String.format("%s/users/admin", apiPrefix)).hasAnyRole(Role.ADMIN)
//                            .requestMatchers(PUT,
//                                    String.format("%s/users", apiPrefix)).permitAll()



                            .requestMatchers("/payment-fail.html", "/payment-success.html", "/**")
                            .permitAll();
                }).csrf(AbstractHttpConfigurer::disable);


        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all paths
                .allowedOrigins("http://localhost:3000") // Allow only this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD") // Allow these methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials (cookies, authorization headers, etc.)
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

}
