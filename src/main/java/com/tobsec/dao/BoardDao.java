package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

public interface BoardDao {
    int insertBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(int boardNo);
    void deleteAll();
    int getAutoValue(String databaseName);
    int countAll();
    int getMaxBoardNo();
    Board getBoard(int boardNo);
    List<Board> getAllBoardList();
    List<Board> getAllBoardListByUserId(String writerId);
    int countBoard(int boardNo);
    void alterBoardNo(int autoInit);
}