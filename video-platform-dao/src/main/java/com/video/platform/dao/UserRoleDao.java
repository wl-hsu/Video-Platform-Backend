package com.video.platform.dao;

import com.video.platform.domain.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleDao {

    List<UserRole> getUserRoleByUserId(Long userId);

    Integer addUserRole(UserRole userRole);
}
