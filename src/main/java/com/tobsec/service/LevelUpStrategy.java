package com.tobsec.service;

import com.tobsec.model.User;

public interface LevelUpStrategy {
    boolean checkLevelUp(User user) ;
}