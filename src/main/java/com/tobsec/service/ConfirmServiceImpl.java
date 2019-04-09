package com.tobsec.service;

import com.tobsec.model.Confirm;
import com.tobsec.dao.*;
import com.tobsec.service.exception.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service("confirmService")
public class ConfirmServiceImpl implements ConfirmService {
    @Autowired
    private ConfirmDao confirmDao;

    @Autowired
    private UserService userService;

    public void addConfirm(Confirm confirm) throws EmptyResultException {
        // 등록된 user인지?
        if( userService.countUser(confirm.getId()) == 1) {
            int confirm_seq = this.getMaxSeq(confirm.getId(), confirm.getConfirm_date()) + 1;

            confirm.setConfirm_seq(confirm_seq);

            confirmDao.addConfirm(confirm);
        } else {
            throw new EmptyResultException("회원으로 등록되어 있는 유저가 아닙니다.(" + confirm.getId() + ")");
        }
    }

    public void deleteConfirm(Confirm confirm) {
        int count = this.countConfirm(confirm.getId(), confirm.getConfirm_date(), confirm.getConfirm_seq());

        if( count == 0 ) {
            if( userService.countUser(confirm.getId()) == 1) {
                confirmDao.deleteConfirm(confirm);
            } else {
                throw new EmptyResultException("회원으로 등록되어 있는 유저가 아닙니다.(" + confirm.getId() + ")");
            }
        } else {
            throw new EmptyResultException("이미 등록되어 있는 항의 내역입니다.");
        }
    }

    public int countUserDate(String id, int confirm_date) {
        return confirmDao.countUserDate(id, confirm_date);
    }

    public int countConfirm(String id, int confirm_date, int confirm_seq) {
        return confirmDao.countConfirm(id, confirm_date, confirm_seq);
    }

    public int getMaxSeq(String id, int confirm_date) {
        int seqno = 0;

        if(this.countUserDate(id, confirm_date) == 0) {
            seqno = 0;
        } else {
            seqno = confirmDao.getMaxSeq(id, confirm_date);
        }
        return seqno;
    }

    public int countAllUser(String id) {
        return confirmDao.countAllUser(id);
    }

    public void deleteAll() {
        confirmDao.deleteAll();
    }

    public void deleteAllByUser(String id) {
        confirmDao.deleteAllUser(id);
    }

}