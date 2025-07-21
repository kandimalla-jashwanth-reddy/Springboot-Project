package com.example.demo.security;

// All Spring Security imports removed for disabling security

// @Configuration
// @EnableWebSecurity
public class SecurityConfig {

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //             .csrf().disable()
    //             .authorizeHttpRequests()
    //             .requestMatchers("/api/auth/**").permitAll()
    //             .anyRequest().authenticated()
    //             .and()
    //             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    //     return http.build();
    // }

    // @Bean
    // public AuthenticationManager authenticationManager(
    //         AuthenticationConfiguration authenticationConfiguration) throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // @Bean
    // public JwtAuthenticationFilter jwtAuthenticationFilter() {
    //     return new JwtAuthenticationFilter();
    // }
}