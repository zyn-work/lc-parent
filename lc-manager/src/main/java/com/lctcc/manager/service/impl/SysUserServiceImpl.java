package com.lctcc.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.lctcc.manager.mapper.SysUserMapper;
import com.lctcc.manager.service.SysUserService;
import com.lctcc.model.dto.system.LoginDto;
import com.lctcc.model.entity.system.SysUser;
import com.lctcc.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //获取提交用户名
        String userName = loginDto.getUserName();
        //根据名字查数据库sys_user表
        SysUser sysUser = sysUserMapper.sysselectUserInfoByUserName(userName);
        //如果用户不存在
        if(sysUser == null){
            throw new RuntimeException("用户名不存在");
        }
        //如果用户存在,获取输入的密码,与数据库密码比较
        String database_password = sysUser.getPassword();
        String input_password =
                DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());;

        if(!input_password.equals(database_password)){
            throw new RuntimeException("密码不正确");
        }

        //登录成功生成token
        String token = UUID.randomUUID().toString().replaceAll("-","");

        redisTemplate.opsForValue()
                .set("user:login"+token,
                        JSON.toJSONString(sysUser),
                        12,
                        TimeUnit.HOURS);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }
}
