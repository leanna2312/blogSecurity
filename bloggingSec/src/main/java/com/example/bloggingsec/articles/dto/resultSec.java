package com.example.bloggingsec.articles.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class resultSec {
    private boolean success;

    public resultSec(boolean success) {
        this.success = success;
    }
}
