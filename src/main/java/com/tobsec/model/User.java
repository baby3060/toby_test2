package com.tobsec.model;

// import lombok.*;

import com.tobsec.common.Password;
import com.tobsec.service.LevelUpStrategy;

/*
@Getter
@Setter
@ToString
// 기본 생성자
@NoArgsConstructor
// 필수 생성자(@NonNull, final)
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
// equals와 hashCode 구현 시 level, login, recommend 필드 제외하고 구현
@EqualsAndHashCode(exclude = {"level", "login", "recommend", "recid"})
*/

public class User {
    /*
    @NonNull private String id;
    @NonNull private String name;
    @NonNull private String password;
    @NonNull private Level level;
    @NonNull private int login;
    @NonNull private int recommend;
    @NonNull private String email;
    */

    public User() {

    }

    public User(String id, String name, String password, Level level, int login, int recommend, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }

    private String id;
    private String name;
    @Password
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;
    private String recid;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public Level getLevel() { return level; }
    public int getLogin() { return login; }
    public int getRecommend() { return recommend; }
    public String getEmail() { return email; }
    public String getRecid() { return recid; }
    
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setLevel(Level level) { this.level = level; }
    public void setLogin(int login) { this.login = login; }
    public void setRecommend(int recommend) { this.recommend = recommend; }
    public void setEmail(String email) { this.email = email; }
    public void setRecid(String recid) { this.recid = recid; }

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
     * 해당 User의 등급이 상승 대상인지?
     * 전략에 따른 구현
     */
    public boolean isLvlUpTarget(LevelUpStrategy upStrategy) {
        return upStrategy.checkLevelUp(this);
    }

    @Override
    public String toString() {
        return "Id : " + this.id + ", Name : " + this.name + ", Password : " + this.password + ", Level : " + this.level.toString() + ", (" + login + ", " + recommend + ")" + ", Email : " + this.email + ", Recid : " + this.recid;
    }

    @Override
    public int hashCode() {
        int result = 0;

        result += this.id.hashCode();
        result += this.name.hashCode();
        result += this.email.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if( obj == null ) {
            return false;
        }

        if( obj == this ) {
            return true;
        }

        if( obj instanceof User ) {
            User temp = (User)obj;

            return (temp.getId().equals(this.id) && temp.getName().equals(this.name) && temp.getEmail().equals(this.email));
        } else {
            return false;
        }
    }
}