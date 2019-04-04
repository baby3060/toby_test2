package com.tobsec.model;

import lombok.*;

@Getter
@Setter
@ToString
// equals와 hashCode 구현 시 level, login, recommend 필드 제외하고 구현
@EqualsAndHashCode(exclude = {"level", "login", "recommend"})
public class User {
    private final int LOGIN_MIN_SILVER = 50;
    private final int RECOMMEND_MIN_GOLD = 30;

    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;

    public User() { }

    public User(String id, String name, String password, Level level, int login, int recommend, String email) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.level = level;
		this.login = login;
		this.recommend = recommend;
		this.email = email;
    }
    
    public void upgradeLevel() {
        // this의 다음 레벨 
        Level nextLevel = this.level.getNextLevel();

        if(nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        } else {    
            this.setLevel(nextLevel);
        }
    }

    /**
     * User의 등급 상승 대상인지?
     */
    public boolean isLvlUpTarget() {
        boolean isSilver = (login >= 50);

        switch(this.level) {
            case BRONZE : 
                return isSilver;
            case SILVER : 
                return isSilver && recommend >= RECOMMEND_MIN_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalStateException("잘못된 등급입니다.");
        }
    }

}