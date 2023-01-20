package com.ll.exam.springsecurityjwt.app.article.controller;

import com.ll.exam.springsecurityjwt.app.article.entity.Article;
import com.ll.exam.springsecurityjwt.app.article.service.ArticleService;
import com.ll.exam.springsecurityjwt.app.base.dto.RsData;
import com.ll.exam.springsecurityjwt.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticlesController {
    private final ArticleService articleService;

    @GetMapping("")
    public ResponseEntity<RsData> list() {
        List<Article> articles = articleService.findAll();

        return Util.spring.responseEntityOf(
                RsData.successOf(
                        Util.mapOf(
                                "articles", articles
                        )
                )
        );
    }
}
