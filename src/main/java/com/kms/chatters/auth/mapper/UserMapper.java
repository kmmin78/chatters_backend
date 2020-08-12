package com.kms.chatters.auth.mapper;

import com.kms.chatters.auth.vo.UserDetailsImpl;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    @Select({
        "select ",
        " member_id as id,",
        " member_password as password,",
        " member_role as role",
        " from tb_member_info",
        " where member_id = #{username}"
    })
    UserDetailsImpl getUserDetails(String username);
    
}