package com.tobsec.model;

import javax.persistence.*;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"approval", "id"})
@EqualsAndHashCode(of = {"approval", "confirm_date", "confirm_seq"})
@Entity
@IdClass(ConfirmKey.class)
public class Confirm {
    @Id
    @Column(name = "id")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User approval;

    public void setApproval(User approval) {
        if( this.approval != null ) {
            this.approval.getConfirmList().remove(this);
        }

        this.approval = approval;
        if( this.approval != null ) {
            this.approval.getConfirmList().add(this);
        }
    }


    @Id
    @Column(name = "confirm_date", precision = 8)
    private int confirm_date;
    
    @Id
    @Column(name= "confirm_seq", precision = 3)
    private int confirm_seq;

    @Column(name = "confirm_time", length = 6)
    private String confirm_time;
    private String content;
    // 운영진이 해결하면서 조치사항 작성
    private String solve_content;
    // 운영진이 확인
    @Column(length = 1)
    private String checkflagad;
    // 유저가 최종 확인
    @Column(length = 1)
    private String checkflagus;
    
    private Timestamp solve_timestamp;   

    public Confirm() { }

    @PrePersist
    public void setupId() {
        if( this.id == null ) {
            if( this.approval != null ) {
                this.id = this.approval.getId();
            }
        }
    }

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