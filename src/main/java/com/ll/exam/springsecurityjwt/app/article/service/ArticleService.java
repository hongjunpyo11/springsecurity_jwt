package com.ll.exam.springsecurityjwt.app.article.service;

import com.ll.exam.springsecurityjwt.app.article.entity.Article;
import com.ll.exam.springsecurityjwt.app.article.repository.ArticleRepository;
import com.ll.exam.springsecurityjwt.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article write(Member author, String subject, String content) {
        Article article = Article.builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        articleRepository.save(article);

        return article;
    }

    public List<Article> findAll() {
        return articleRepository.findAllByOrderByIdDesc();
    }
}