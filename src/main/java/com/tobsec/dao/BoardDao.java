package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

public interface BoardDao {
    public int insertBoard(Board board);
    public void updateBoard(Board board);
    public void deleteBoard(int boardNo);
    public void deleteAll();
    public int getAutoValue(String databaseName);
    public int countAll();
    public int getMaxBoardNo();
    public Board getBoard(int boardNo);
    public List<Board> getAllBoardList();
    public List<Board> getAllBoardListByUserId(String writerId);
}