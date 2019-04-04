package com.tobsec.service;

import com.tobsec.model.User;

import com.tobsec.service.exception.*;

public interface UserService {
    public void addUser(User user) throws EmptyResultException;
    public void updateUser(User user) throws EmptyResultException;
    public void deleteUser(User user) throws EmptyResultException;
    public void deleteAll();

    public int countUser(String id);

    public int countAll();
}