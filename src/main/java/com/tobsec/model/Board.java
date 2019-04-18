package com.tobsec.model;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"writerId", "content", "boardGubun", "writeTime"})
public class Board {
    private int boardNo;
    @NonNull private String writerId;
    private String content;
    @NonNull private int boardGubun;
    private Timestamp writeTime;
}