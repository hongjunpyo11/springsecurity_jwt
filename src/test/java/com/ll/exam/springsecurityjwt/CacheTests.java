package com.ll.exam.springsecurityjwt;

import com.ll.exam.springsecurityjwt.app.cacheTest.service.CacheTestService;
import com.ll.exam.springsecurityjwt.app.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CacheTests {

    @Autowired
    private CacheTestService cacheTestService;

    @Test
    @DisplayName("캐시 사용")
    void t1() throws Exception {
        int rs = cacheTestService.getCachedInt();
        System.out.println(rs);

        rs = cacheTestService.getCachedInt();
        System.out.println(rs);
    }

    @Test
    @DisplayName("캐시 삭제")
    void t2() throws Exception {
        int rs = cacheTestService.getCachedInt();
        System.out.println(rs);

        rs = cacheTestService.getCachedInt();
        System.out.println(rs);

        cacheTestService.deleteCacheKey1();

        rs = cacheTestService.getCachedInt();
        System.out.println(rs);
    }

    @Test
    @DisplayName("캐시 수정")
    void t3() throws Exception {
        int rs = cacheTestService.getCachedInt();
        System.out.println(rs);

        rs = cacheTestService.getCachedInt();
        System.out.println(rs);

        cacheTestService.putCacheKey1();

        rs = cacheTestService.getCachedInt();
        System.out.println(rs);
    }

    @Test
    @DisplayName("더하기 캐시")
    void t4() throws Exception {
        int rs = cacheTestService.plus(10, 20);
        System.out.println(rs);

        rs = cacheTestService.plus(5, 2);
        System.out.println(rs);

        rs = cacheTestService.plus(10, 20);
        System.out.println(rs);
    }
}
