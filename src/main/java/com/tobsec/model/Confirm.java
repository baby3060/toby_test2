package com.tobsec.model;

import javax.persistence.*;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"approval", "confirm_date", "confirm_seq"})
public class Confirm {
    private String id;
    private User approval;
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

    public Confirm(User approval, int confirm_date, String content) {
        this.approval = approval;
        this.confirm_date = confirm_date;
        this.content = content;
    }

    public Confirm(User approval, int confirm_date, int confirm_seq, String content) {
        this.approval = approval;
        this.confirm_date = confirm_date;
        this.confirm_seq = confirm_seq;
        this.content = content;
    }

}