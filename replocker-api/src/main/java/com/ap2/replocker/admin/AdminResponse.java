package com.ap2.replocker.admin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {
    private String id;
    private String username;
    private String email;
}
