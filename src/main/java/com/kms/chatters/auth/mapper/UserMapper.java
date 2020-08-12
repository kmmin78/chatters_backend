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
        " member_role as role,",
        " member_name as member_name",
        " from tb_member_info",
        " where member_id = #{username}"
    })
    UserDetailsImpl getUserDetails(String username);

    // @Select({
    //     "select 'admin' as id, '123' as password, 'ADMIN' as role from dual"
    // })
    // UserDetailsImpl getUserDetails(String username);
}