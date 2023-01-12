package com.example.bloggingsec.articles.entity;

public enum UserRoleEnum {

    USER(Authority.USER), // 사용자 권한
    ADMIN(Authority.ADMIN); // 관리자 권한

    //
    // authority 왜 설정?
    //
    private final String authority;
    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }



    public static class Authority{ //권한 분배 클래스
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
