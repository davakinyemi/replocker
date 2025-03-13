package com.ap2.replocker.admin;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminMapper {
    public Admin fromTokenAttributes(Map<String, Object> attributes) {
        Admin admin = new Admin();

        if (attributes.containsKey("sub")) {
            admin.setId(attributes.get("sub").toString());
        }

        if (attributes.containsKey("given_name")) {
            admin.setUsername(attributes.get("given_name").toString());
        } else if (attributes.containsKey("nickname")) {
            admin.setUsername(attributes.get("nickname").toString());
        }

        if (attributes.containsKey("email")) {
            admin.setEmail(attributes.get("email").toString());
        }

        return admin;
    }
}
