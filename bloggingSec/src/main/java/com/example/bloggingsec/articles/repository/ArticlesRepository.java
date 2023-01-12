package com.example.bloggingsec.articles.repository;

import com.example.bloggingsec.articles.entity.Articles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface ArticlesRepository extends JpaRepository<Articles, Long> {

    List<Articles> findAllByOrderByCreateAtDesc();

    Articles findByTitle(String title);
}
