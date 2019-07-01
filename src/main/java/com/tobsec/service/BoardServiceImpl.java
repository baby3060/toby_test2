package com.tobsec.service;

import java.util.List;

import com.tobsec.dao.*;
import com.tobsec.common.Log;
import com.tobsec.model.Board;

import com.tobsec.service.exception.*;
import com.tobsec.common.JpaTransaction;
import org.slf4j.Logger;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Transactional
@Service("boardService")
public class BoardServiceImpl implements BoardService {
    @Log
    public Logger boardLogger;

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private UserService userService;

    public void addBoard(Board board) {
        if( board.getWriter() == null || board.getWriter().getId().equals("") ) {
            throw new KeyNullException("작성자가 비어있습니다.");
        } else {
            if( userService.countUser(board.getWriter().getId()) == 1 ) {
                Long newBoardNo = boardDao.insertBoard(board);

                board.setBoardNo(newBoardNo);

                boardLogger.info("새로 생성된 Board의 번호 : " + newBoardNo + ", " + board.toString());
            } else {
                throw new EmptyResultException("등록된 User가 아닙니다(" + board.getWriter().getId() + ").");
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

                if( this.countAll() == 0 ) {
                    boardDao.alterBoardNo(0L);
                } else {
                    // 가장 큰 BoardNo와 삭제한 BoardNo가 같다면, 1 감소.
                    if( this.getMaxBoardNo() == board.getBoardNo() ) {
                        boardDao.alterBoardNo(this.getMaxBoardNo() - 1L);
                    }
                }
            } else {
                throw new EmptyResultException("등록된 게시물이 아닙니다(" + board.getBoardNo() + ").");
            }
        }
        
    }

    public Long getMaxBoardNo() {
        Long maxBoardNo = 0L;
        
        if( boardDao.countAll() == 0 ) {
            maxBoardNo = 0L;
        } else {
            maxBoardNo = boardDao.getMaxBoardNo();
        }
        
        boardLogger.debug("BoardDao GetMaxSeqno : " + maxBoardNo);
        
        return maxBoardNo;
    }

    public void deleteAll() {
        
        boardDao.deleteAll();

        boardDao.alterBoardNo(0L);
        
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

    public Board getBoard(Long boardNo) {
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