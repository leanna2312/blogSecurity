package com.example.bloggingsec.articles.dto;

import lombok.Getter;



@Getter
public class resultFst {

    private Boolean success;
    private Object data;

    public resultFst(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }
}