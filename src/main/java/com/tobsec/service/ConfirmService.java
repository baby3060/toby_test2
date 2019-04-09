package com.tobsec.service;

import com.tobsec.model.Confirm;

import com.tobsec.service.exception.*;

public interface ConfirmService {
    public void addConfirm(Confirm confirm) throws EmptyResultException;
    public void deleteConfirm(Confirm confirm);
    public int countAllUser(String id);
    public int countUserDate(String id, int confirm_date);
    public int countConfirm(String id, int confirm_date, int confirm_seq);
    public int getMaxSeq(String id, int confirm_date);
    public void deleteAll();
    public void deleteAllByUser(String id);
}