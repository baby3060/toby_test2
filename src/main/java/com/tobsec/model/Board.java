package com.tobsec.model;

import java.sql.Timestamp;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"content", "boardGubun", "writeTime"})
@Entity
public class Board {
    @Id
    @Column(name = "board_no", precision = 8)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNo;

    @NonNull 
    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;
    public void setWriter(User writer) {
        if( this.writer != null ) {
            this.writer.getBoardList().remove(this);
        }

        this.writer = writer;
        if( this.writer != null ) {
            this.writer.getBoardList().add(this);
        }
    }


    private String content;

    @NonNull 
    @Column(name = "board_gubun", precision = 1)
    private int boardGubun;

    @Column(name = "write_time")
    private Timestamp writeTime;
    
}