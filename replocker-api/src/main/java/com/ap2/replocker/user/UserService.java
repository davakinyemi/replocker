package com.ap2.replocker.user;

import com.ap2.replocker.report_collection.access_token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccessTokenService tokenService;

    public UserResponse createOrGetUser(UserRequest userRequest) {
        return this.userRepository.findByEmail(userRequest.email())
                .map(this.userMapper::toUserResponse)
                .orElseGet(() -> this.createNewUser(userRequest.email()));

    }

    private UserResponse createNewUser(String email) {
        User newUser = User.builder()
                .email(email)
                .isActive(true)
                .build();
        return this.userMapper.toUserResponse(userRepository.save(newUser));
    }
}
