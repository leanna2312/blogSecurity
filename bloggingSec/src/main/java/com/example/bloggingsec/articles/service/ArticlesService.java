package com.example.bloggingsec.articles.service;

import com.example.bloggingsec.articles.dto.*;
import com.example.bloggingsec.articles.entity.Articles;
import com.example.bloggingsec.articles.repository.ArticlesRepository;
//import com.example.bloggingsec.JwtUtil;
import com.example.bloggingsec.Member;
import com.example.bloggingsec.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

//import static com.example.bloggingsec.member.entity.MemberGrant.*;

@Service
@RequiredArgsConstructor
public class ArticlesService {

    private final ArticlesRepository articlesRepository;
    private final MemberRepository memberRepository;

   // private final JwtUtil jwtUtil;

    @Transactional
    public ArticleResponseDto create(ArticleRequestDto boardForm, HttpServletRequest request) {
//        String token = jwtUtil.resolveToken(request);
//
//        if (token == null) {
//            return null;
//        }
//
//        if (!jwtUtil.validateToken(token)) {
//            return null;
//        }
//
//        Claims claims = jwtUtil.getUserInfoFromToken(token);
//        Member member = memberRepository.findByUsername().orElseThrow();
//
 //       Articles board = new Articles(boardForm, member);
        Articles saveBoard = articlesRepository.saveAndFlush(new Articles(boardForm));

        return new ArticleResponseDto(saveBoard);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponseDto> readAll() {
        List<Articles> boards = articlesRepository.findAllByOrderByCreateAtDesc()
                .stream().filter(bulletinBoard -> bulletinBoard.getIsDeleted() == null).collect(Collectors.toList());

        return (List<ArticleResponseDto>) boards.stream().map(bulletinBoard -> new ArticleResponseDto(bulletinBoard));

    }

    public ArticleResponseDto readOne(Long id) {
        Articles board = articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (board.getIsDeleted() != null) {
            throw new IllegalArgumentException("삭제된 게시글입니다.");
        }

        return new ArticleResponseDto(board);
    }

    @Transactional
    public resultSec Delete(Long id, HttpServletRequest request) throws IllegalAccessException {
        Articles board = articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

//        String token = jwtUtil.resolveToken(request);
//        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글은 존재하지 않습니다.")
        );
//        if (token == null) {
//            return null;
//        }
//
//        if (!jwtUtil.validateToken(token) || member.getRole().equals(ADMIN)) {
//            return null;
//        }

        if (!board.getMember().getId().equals(member)) {
            throw new IllegalAccessException("회원 만 삭제/수정할 수 있습니다.");
        }

        board.softDelete(true);

        return new resultSec(true);
    }

    @Transactional
    public resultFst update(Long id, ArticleRequestDto boardForm, HttpServletRequest request) throws IllegalAccessException {
        Articles board = articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

//        String token = jwtUtil.resolveToken(request);
//        Claims claims = jwtUtil.getUserInfoFromToken(token);
//        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 글은 존재하지 않습니다.")
        );
//
//        if (token == null) {
//        }
//
//        if (!jwtUtil.validateToken(token) || member.getRole().equals(ADMIN)) {
//        }

        if (!board.getMember().getId().equals(member.getId())) {
            throw new IllegalAccessException("회원 만 삭제/수정할 수 있습니다.");
        }

        board.update(boardForm);

        return new resultFst(true, new ArticleResponseDto(board));
    }
}
