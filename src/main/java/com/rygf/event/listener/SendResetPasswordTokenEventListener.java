package com.rygf.event.listener;

import com.rygf.common.GetLink;
import com.rygf.entity.ResetPasswordToken;
import com.rygf.entity.User;
import com.rygf.event.SendResetPasswordTokenEvent;
import com.rygf.service.VerificationTokenService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SendResetPasswordTokenEventListener implements ApplicationListener<SendResetPasswordTokenEvent> {
    
    private final VerificationTokenService tokenService;
    private final MailSender mailSender;
    private SendResetPasswordTokenEvent event;
    
    @Value("${support.mail}")
    private String supportMail;
    
    @Async // muốn catch Exception --> tắt Async
    @Override
    public void onApplicationEvent(SendResetPasswordTokenEvent event) throws MailException {
        this.event = event;
        final String token = UUID.randomUUID().toString();
        tokenService.saveToken(new ResetPasswordToken(token, event.getUser()));
        sendConfirmationMail(token);
    }
    
    @Async // muốn catch Exception --> tắt Async
    public void sendConfirmationMail(String token) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        User user = event.getUser();
        
        mail.setFrom(supportMail);
        mail.setTo(user.getEmail());
        mail.setSubject("Review Your Girl Friend | Reset Password Token for : " + user.getEmail());
        mail.setText("Here is your verification token : " + event.getServerUrl() + GetLink.CONFIRM_RESET_PASSWORD_TOKEN_URI + "?token=" + token);
        mailSender.send(mail);
    }
}
