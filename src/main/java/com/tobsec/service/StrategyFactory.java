package com.tobsec.service;

import com.tobsec.model.User;
import com.tobsec.model.Level;

/**
 * 사용자 등급 정책 모아놓은 팩토리
 */
public class StrategyFactory {
    
    public static LevelUpStrategy defaultStrategy() {
        final int LOGIN_MIN_SILVER = 50;
        final int RECOMMEND_MIN_GOLD = 30;

        return new LevelUpStrategy() {
            public boolean checkLevelUp(User user) throws IllegalStateException {
                boolean isSilver = user.getLogin() >= LOGIN_MIN_SILVER;
                boolean isGold = (isSilver && (user.getRecommend() >= RECOMMEND_MIN_GOLD));

                switch(user.getLevel()) {
                    case BRONZE : 
                        return isSilver;
                    case SILVER : 
                        return isGold;
                    case GOLD:
                        return false;
                    default:
                        throw new IllegalStateException("잘못된 등급입니다.");
                }
            }
        };
    }

}