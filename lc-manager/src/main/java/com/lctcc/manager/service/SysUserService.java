package com.lctcc.manager.service;

import com.lctcc.model.dto.system.LoginDto;
import com.lctcc.model.vo.system.LoginVo;

public interface SysUserService {

    LoginVo login(LoginDto loginDto);
}
