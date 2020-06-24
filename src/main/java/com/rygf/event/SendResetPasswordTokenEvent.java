package com.rygf.event;

import com.rygf.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendResetPasswordTokenEvent extends ApplicationEvent {
    private final String serverUrl;
    private final User user;
    
    public SendResetPasswordTokenEvent(Object source, final User user, String serverUrl) {
        super(source);
        this.serverUrl = serverUrl;
        this.user = user;
    }
}
