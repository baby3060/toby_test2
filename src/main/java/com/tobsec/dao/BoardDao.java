package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

public interface BoardDao {
    void insertBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(Long boardNo);
    void deleteAll();
    int getAutoValue(String databaseName);
    int countAll();
    Long getMaxBoardNo();
    Board getBoard(Long boardNo);
    List<Board> getAllBoardList();
    List<Board> getAllBoardListByUserId(String writerId);
    int countBoard(Long boardNo);
    void alterBoardNo(Long autoInit);
}