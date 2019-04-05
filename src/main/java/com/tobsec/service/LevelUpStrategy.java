package com.tobsec.service;

import com.tobsec.model.User;

public interface LevelUpStrategy {
    public boolean checkLevelUp(User user) ;
}