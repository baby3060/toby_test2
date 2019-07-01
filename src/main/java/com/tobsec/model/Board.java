package com.tobsec.model;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"writer", "id"})
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"content", "boardGubun", "writeTime"})
@Entity
@TableGenerator(name = "BOARD_SEQ_GENERATOR", table = "TABLE_SEQ_KEY",
                     pkColumnValue = "board_seq", valueColumnName = "board_seq_val", initialValue = 0, allocationSize = 1)                
public class Board {

    @Id
    @Column(name = "board_no", precision = 8)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BOARD_SEQ_GENERATOR")
    private Long boardNo;

    @Column(name = "writer_id")
    private String id;

    
    @NonNull
    @ManyToOne(fetch=FetchType.LAZY , cascade=CascadeType.ALL)
    @JoinColumn(name = "writer_id", insertable = false, updatable = false)
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
    

    @PrePersist
    public void setupId() {
        
        if( this.id == null ) {
            if( this.writer != null ) {
                this.id = this.writer.getId();
            }
        }
        
        writeTime = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void updateId() {
        if( this.id == null ) {
            if( this.writer != null ) {
                this.id = this.writer.getId();
            }
        }
    }

    @NonNull 
    private String content;

    @Column(name = "board_gubun", precision = 1)
    @NonNull
    private int boardGubun;

    @Column(name = "write_time")
    private Timestamp writeTime;
    
}