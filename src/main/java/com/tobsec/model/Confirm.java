package com.tobsec.model;

import java.sql.Timestamp;

// import lombok.*;

/*
@Getter
@Setter
@ToString
// equals와 hashCode 구현 시 level, login, recommend 필드 제외하고 구현
@EqualsAndHashCode(exclude = {"confirm_time", "content", "solve_content", "checkflagad", "checkflagus", "solve_timestamp"})
*/
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

    
    public String getId() { return id; }
    public int getConfirm_date() { return confirm_date; }
    public int getConfirm_seq() { return confirm_seq; }
    public String getConfirm_time() { return confirm_time; }
    public String getContent() { return content; }
    public String getSolve_content() { return solve_content; }
    public String getCheckflagad() { return checkflagad; }
    public String getCheckflagus() { return checkflagus; }
    public Timestamp getSolve_timestamp() { return solve_timestamp; }

    public void setId(String id) { this.id = id; }
    public void setConfirm_date(int confirm_date) { this.confirm_date = confirm_date; }
    public void setConfirm_seq(int confirm_seq) { this.confirm_seq = confirm_seq; }
    public void setConfirm_time(String confirm_time) { this.confirm_time = confirm_time; }
    public void setContent(String content) { this.content = content; }
    public void setSolve_content(String solve_content) { this.solve_content = solve_content; }
    public void setCheckflagad(String checkflagad) { this.checkflagad = checkflagad; }
    public void setCheckflagus(String checkflagus) { this.checkflagus = checkflagus; }
    public void setSolve_timestamp(Timestamp solve_timestamp) { this.solve_timestamp = solve_timestamp; }
    
    public Confirm() { }

    public Confirm(String id, int confirm_date, String content) {
        this.id = id;
        this.confirm_date = confirm_date;
        this.content = content;
    }

    public Confirm(String id, int confirm_date, int confirm_seq, String content) {
        this.id = id;
        this.confirm_date = confirm_date;
        this.confirm_seq = confirm_seq;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Id : " + this.id + "(" + this.confirm_date + ", " + this.confirm_seq + "), Write Time : " + this.confirm_time + ", Content : " 
                       + this.content + ", Solve_content : " + this.solve_content + ", (" + this.checkflagad + ", " + this.checkflagus + ")";
    }

    @Override
    public int hashCode() {
        int result = 0;

        result += this.id.hashCode();
        result += this.confirm_date;
        result += this.confirm_seq;

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

        if( obj instanceof Confirm ) {
            Confirm temp = (Confirm)obj;

            return (temp.getId().equals(this.id) && (temp.getConfirm_date() == this.confirm_date) && (temp.getConfirm_seq() == this.confirm_seq));
        } else {
            return false;
        }
    }
}