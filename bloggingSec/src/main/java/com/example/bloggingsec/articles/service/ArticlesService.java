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
        Articles board = articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ??????????????????."));

        if (board.getIsDeleted() != null) {
            throw new IllegalArgumentException("????????? ??????????????????.");
        }

        return new ArticleResponseDto(board);
    }

    @Transactional
    public resultSec Delete(Long id, HttpServletRequest request) throws IllegalAccessException {
        Articles board = articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ??????????????????."));

//        String token = jwtUtil.resolveToken(request);
//        Claims claims = jwtUtil.getUserInfoFromToken(token);
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("?????? ?????? ???????????? ????????????.")
        );
//        if (token == null) {
//            return null;
//        }
//
//        if (!jwtUtil.validateToken(token) || member.getRole().equals(ADMIN)) {
//            return null;
//        }

        if (!board.getMember().getId().equals(member)) {
            throw new IllegalAccessException("?????? ??? ??????/????????? ??? ????????????.");
        }

        board.softDelete(true);

        return new resultSec(true);
    }

    @Transactional
    public resultFst update(Long id, ArticleRequestDto boardForm, HttpServletRequest request) throws IllegalAccessException {
        Articles board = articlesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ??????????????????."));

//        String token = jwtUtil.resolveToken(request);
//        Claims claims = jwtUtil.getUserInfoFromToken(token);
//        Member member = memberRepository.findByUsername(claims.getSubject()).orElseThrow();
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NullPointerException("?????? ?????? ???????????? ????????????.")
        );
//
//        if (token == null) {
//        }
//
//        if (!jwtUtil.validateToken(token) || member.getRole().equals(ADMIN)) {
//        }

        if (!board.getMember().getId().equals(member.getId())) {
            throw new IllegalAccessException("?????? ??? ??????/????????? ??? ????????????.");
        }

        board.update(boardForm);

        return new resultFst(true, new ArticleResponseDto(board));
    }
}
