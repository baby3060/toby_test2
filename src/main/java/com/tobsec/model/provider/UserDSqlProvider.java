package com.tobsec.model.provider;

import org.apache.ibatis.jdbc.SQL;

public class UserDSqlProvider {
    public String selectDynamic(String orderFld, String orderAlg) {
        return new SQL() {{
            SELECT("a.*, b.*");
            FROM("USER a");
            JOIN("board b On (a.id = b.writer_id)");
            ORDER_BY(orderFld + " " + orderAlg);
        }}.toString();
    }
}
