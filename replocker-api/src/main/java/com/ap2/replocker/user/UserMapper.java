package com.ap2.replocker.user;

import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .build();
    }
}
