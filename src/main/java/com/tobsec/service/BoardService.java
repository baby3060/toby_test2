package com.tobsec.service;

import java.util.List;

import com.tobsec.model.Board;

public interface BoardService {
    void addBoard(Board board);
    void updateContent(Board board);
    void deleteBoard(Board board);
    void deleteAll();
    int countAll();
    int countBoard(Board board);
    void deleteByWriter(String writerId);
    int countByWriter(String writerId);
    Board getBoard(Long boardNo);
    Long getMaxBoardNo();
    int getIncreValue(String dbName);
    List<Board> selectAll();
    List<Board> selectAllByWriter(String writerId);
}