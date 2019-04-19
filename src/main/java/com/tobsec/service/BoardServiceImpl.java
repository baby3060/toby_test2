package com.tobsec.service;

import java.util.List;

import com.tobsec.dao.*;
import com.tobsec.common.Log;
import com.tobsec.model.Board;

import com.tobsec.service.exception.*;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
    @Log
    private Logger boardLogger;

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private UserService userService;

    public void addBoard(Board board) {
        if( board.getWriterId() == null || board.getWriterId().equals("") ) {
            throw new KeyNullException("작성자가 비어있습니다.");
        } else {
            if( userService.countUser(board.getWriterId()) == 1 ) {
                int newBoardNo = boardDao.insertBoard(board);

                boardLogger.info("새로 생성된 Board의 번호 : " + newBoardNo);
            } else {
                throw new EmptyResultException("등록된 User가 아닙니다(" + board.getWriterId() + ").");
            }
        }
    }

    public int countBoard(Board board) {
        return boardDao.countBoard(board.getBoardNo());
    }

    public void updateContent(Board board) {
        if( board == null || board.getBoardNo() <= 0 ) {
            throw new KeyNullException("수정할 게시물 참조가 비어있습니다.");
        } else {
            if( this.countBoard(board) == 1 ) {
                boardDao.updateBoard(board);
            } else {
                throw new EmptyResultException("등록된 게시물이 아닙니다(" + board.getBoardNo() + ").");
            }
        }
    }

    public void deleteBoard(Board board) {
        if( board == null || board.getBoardNo() <= 0 ) {
            throw new KeyNullException("삭제할 게시물 참조가 비어있습니다.");
        } else {
            if( this.countBoard(board) == 1 ) {
                boardDao.deleteBoard(board.getBoardNo());

                boardDao.alterBoardNo(this.getMaxBoardNo() + 1);
            } else {
                throw new EmptyResultException("등록된 게시물이 아닙니다(" + board.getBoardNo() + ").");
            }
        }
    }

    public int getMaxBoardNo() {
        int maxBoardNo = 0;

        if( this.countAll() == 0 ) {
            maxBoardNo = 0;
        } else {
            maxBoardNo = boardDao.getMaxBoardNo();
        }

        return maxBoardNo;
    }

    public void deleteAll() {
        boardDao.deleteAll();
    }

    public int countAll() {
        return boardDao.countAll();
    }

    public void deleteByWriter(String writerId) {
        List<Board> listByWriter = boardDao.getAllBoardListByUserId(writerId);

        if(listByWriter.size() > 0) {
            for(Board board : listByWriter) {
                boardDao.deleteBoard(board.getBoardNo());
            }
            boardDao.alterBoardNo(this.getMaxBoardNo() + 1);
        }
    }

    public int countByWriter(String writerId) {
        return this.selectAllByWriter(writerId).size();
    }

    public int getIncreValue(String dbName) {
        return boardDao.getAutoValue(dbName);
    }

    public Board getBoard(int boardNo) {
        if( boardDao.countBoard(boardNo) == 1 ) {
            return boardDao.getBoard(boardNo);
        } else {
            throw new EmptyResultException("등록된 게시물이 아닙니다(" + boardNo + ").");
        }
    }

    public List<Board> selectAll() {
        return boardDao.getAllBoardList();
    }

    public List<Board> selectAllByWriter(String writerId) {
        return boardDao.getAllBoardListByUserId(writerId);
    }
}