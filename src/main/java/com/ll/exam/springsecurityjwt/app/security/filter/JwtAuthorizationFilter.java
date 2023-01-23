package com.ll.exam.springsecurityjwt.app.security.filter;

import com.ll.exam.springsecurityjwt.app.member.entity.Member;
import com.ll.exam.springsecurityjwt.app.member.service.MemberService;
import com.ll.exam.springsecurityjwt.app.security.entity.MemberContext;
import com.ll.exam.springsecurityjwt.app.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            String token = bearerToken.substring("Bearer ".length());

            // 1차 체크(정보가 변조되지 않았는지 체크)
            if (jwtProvider.verify(token)) {
                Map<String, Object> claims = jwtProvider.getClaims(token);

                // 캐시(레디스)를 통해서
                Member member = memberService.findByUsername((String) claims.get("username")).get();

                // 2차 체크(화이트리스트에 포함되는지)
                if ( memberService.verifyWithWhiteList(member, token) ) {
                    forceAuthentication(member);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        null,
                        member.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}

/**
 * 문제 1
 * JWT 토큰은 구조상 폐기가 불가능하고, 품고 있는 정보를 변경할 수 없다.
 * 그렇기 때문에 그 안에 닉네임 또는 이메일 같은 정보가 토큰 내부에 저장되어 있고,
 * 토큰 발급 이후에 사용자가 이메일이나 닉네임을 변경했다면 정보 불일치가 발생한다.
 *
 * 문제 2
 * JWT 토큰의 만료기간이 90일이라고 가정하자.
 * 해당 JWT 토큰자체를 탈취 당한다면, 90일 동안은 뭔가 할 수 있는 방법이 없다.
 * 왜냐하면 JWT 토큰은 구조상 폐기가 불가능 하기 때문이다.
 */