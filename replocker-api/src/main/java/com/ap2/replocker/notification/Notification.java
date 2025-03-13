package com.ap2.replocker.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private String requestId;
    private String requestContent;
    private String userId;
    private String adminId;
    private String requestTitle;
}
