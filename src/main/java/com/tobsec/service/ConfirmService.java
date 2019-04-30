package com.tobsec.service;

import com.tobsec.model.Confirm;

import com.tobsec.service.exception.*;

public interface ConfirmService {
    void addConfirm(Confirm confirm) throws EmptyResultException;
    void deleteConfirm(Confirm confirm);
    int countAllUser(String id);
    int countUserDate(String id, int confirm_date);
    int countConfirm(String id, int confirm_date, int confirm_seq);
    int getMaxSeq(String id, int confirm_date);
    void deleteAll();
    void deleteAllByUser(String id);
}