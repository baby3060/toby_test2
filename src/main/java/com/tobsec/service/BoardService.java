package com.tobsec.service;

import java.util.List;

import com.tobsec.model.Board;

public interface BoardService {
    public void addBoard(Board board);
    public void updateContent(Board board);
    public void deleteBoard(Board board);
    public void deleteAll();
    public int countAll();
    public int countBoard(Board board);
    public void deleteByWriter(String writerId);
    public int countByWriter(String writerId);
    public Board getBoard(int boardNo);
    public int getMaxBoardNo();
    public int getIncreValue(String dbName);
    public List<Board> selectAll();
    public List<Board> selectAllByWriter(String writerId);
}