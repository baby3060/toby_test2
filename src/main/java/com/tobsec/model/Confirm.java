package com.tobsec.model;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@ToString
// equals와 hashCode 구현 시 level, login, recommend 필드 제외하고 구현
@EqualsAndHashCode(exclude = {"confirm_time", "content", "solve_content", "checkflagad", "checkflagus", "solve_timestamp"})
public class Confirm {
    private String id;
    private int confirm_date;
    private int confirm_seq;
    private String confirm_time;
    private String content;
    // 운영진이 해결하면서 조치사항 작성
    private String solve_content;
    // 운영진이 확인
    private String checkflagad;
    // 유저가 최종 확인
    private String checkflagus;
    private Timestamp solve_timestamp;   

    public Confirm() { }

    public Confirm(String id, int confirm_date, String content) {
        this.id = id;
        this.confirm_date = confirm_date;
        this.content = content;
    }

    /**
     * 테스트 용
     */
    public Confirm(String id, int confirm_date, int confirm_seq, String content) {
        this.id = id;
        this.confirm_date = confirm_date;
        this.confirm_seq = confirm_seq;
        this.content = content;
    }
}