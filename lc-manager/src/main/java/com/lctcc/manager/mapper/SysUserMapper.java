package com.lctcc.manager.mapper;

import com.lctcc.model.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {
    SysUser sysselectUserInfoByUserName(String userName);
}
