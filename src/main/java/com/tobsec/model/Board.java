package com.tobsec.model;

import java.sql.Timestamp;

/*
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"writerId", "content", "boardGubun", "writeTime"})
*/

public class Board {
    /*
    private int boardNo;
    @NonNull private String writerId;
    private String content;
    @NonNull private int boardGubun;
    private Timestamp writeTime;
    */

    public Board() {}

    public Board(String writerId, int boardGubun) {
        this.writerId = writerId;
        this.boardGubun = boardGubun;
    }

    private int boardNo;
    private String writerId;
    private String content;
    private int boardGubun;
    private Timestamp writeTime;

    public int getBoardNo() { return boardNo; }
    public String getWriterId() { return writerId; }
    public String getContent() { return content; }
    public int getBoardGubun() { return boardGubun; }
    public Timestamp getWriteTime() { return writeTime; }

    public void setBoardNo(int boardNo) { this.boardNo = boardNo; }
    public void setWriterId(String writerId) { this.writerId = writerId; }
    public void setContent(String content) { this.content = content; }
    public void setBoardGubun(int boardGubun) { this.boardGubun = boardGubun; }
    public void setWriteTime(Timestamp writeTime) { this.writeTime = writeTime; }

    @Override
    public String toString() {
        return "boardNo : " + this.boardNo + "(" + this.boardGubun + "), Write Id : " + this.writerId + ", Content : " 
                       + this.content;
    }

    @Override
    public int hashCode() {
        int result = 0;

        result += this.boardNo;
        result += this.writerId.hashCode();

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

        if( obj instanceof Board ) {
            Board temp = (Board)obj;

            return temp.getWriterId().equals(this.writerId) && (temp.getBoardNo() == this.boardNo);
        } else {
            return false;
        }
    }
}