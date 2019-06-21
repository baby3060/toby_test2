package com.tobsec.model;

import lombok.*;

import com.tobsec.common.Password;
import com.tobsec.service.LevelUpStrategy;

import javax.persistence.*;
import java.util.*;

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
@Entity
public class User {
    
    @Id
    @Column(length = 10)
    @NonNull 
    private String id;

    @NonNull 
    @Column(length = 20)
    private String name;

    @Password 
    @NonNull 
    @Column(length = 1000)
    private String password;

    @NonNull 
    private Level level;

    @NonNull 
    @Column(precision = 8)
    private int login;

    @NonNull 
    @Column(precision = 8)
    private int recommend;

    @NonNull 
    @Column(length = 50)
    private String email;

    @JoinColumn(name = "recid", columnDefinition="varchar(10)")
    private String recid;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Board> boardList;

    @OneToMany(mappedBy = "approval", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Confirm> confirmList;

    {
        boardList = new ArrayList<Board>();
        confirmList = new ArrayList<Confirm>();
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
     * 해당 User의 등급이 상승 대상인지?
     * 전략에 따른 구현
     * @param upStrategy : 넘겨받는 전략
     * @return 레벨업 대상인가?
     */
    public boolean isLvlUpTarget(LevelUpStrategy upStrategy) {
        return upStrategy.checkLevelUp(this);
    }

}