package com.gp.webdriverapi.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.webdriverapi.common.pojo.WdUser;
import com.gp.webdriverapi.system.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, WdUser> {
}
