package com.connectinghands.service.auth;

import com.connectinghands.dto.user.LoginDto;
import com.connectinghands.dto.user.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
} 