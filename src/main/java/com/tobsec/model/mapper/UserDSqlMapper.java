package com.tobsec.model.mapper;

import java.util.List;

import com.tobsec.model.User;
import com.tobsec.model.provider.UserDSqlProvider;

import org.apache.ibatis.annotations.SelectProvider;

public interface UserDSqlMapper {
    @SelectProvider(type=UserDSqlProvider.class, method="selectDynamic")
    List<User> selectList(String orderFld, String orderAlg);
}
