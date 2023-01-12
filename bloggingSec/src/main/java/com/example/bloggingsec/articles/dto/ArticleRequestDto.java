package com.example.bloggingsec.articles.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ArticleRequestDto {

    private String title;
    private String contents;

    public ArticleRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }


}
