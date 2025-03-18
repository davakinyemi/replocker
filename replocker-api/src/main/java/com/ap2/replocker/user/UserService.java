package com.ap2.replocker.user;

import com.ap2.replocker.report_collection.access_token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccessTokenService tokenService;

    public UserResponse createOrGetUser(UserRequest userRequest) {
        String hashedEmail = this.userMapper.hashEmail(userRequest.email());
        return this.userRepository.findByHashedEmail(hashedEmail)
                .map(this.userMapper::toUserResponse)
                .orElseGet(() -> this.createNewUser(hashedEmail));

    }

    private UserResponse createNewUser(String hashedEmail) {
        User newUser = User.builder()
                .hashedEmail(hashedEmail)
                .isActive(true)
                .build();
        return this.userMapper.toUserResponse(userRepository.save(newUser));
    }
}
