package com.tobsec.dao;

import com.tobsec.model.Board;

import java.util.List;

public interface BoardDao {
    public int insertBoard(Board board);
    public void updateBoard(Board board);
    public void deleteBoard(int boardNo);
}