package com.gamza.study.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final RedisTemplate<String, String> redisTemplate;
    private final DefaultMessageService messageService;

    @Value("${coolsms.sender-number}")
    private String senderNumber;

    @Override
    @Transactional
    public void sendAuthCode(String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("[^0-9]","");

        String code = generateAuthCode();

        Message message = new Message();
        message.setFrom(senderNumber);
        message.setTo(phoneNumber);
        message.setText("[GamzaFrontStudy] 인증번호 [" + code + "] 를 입력해주세요.");

        messageService.sendOne(new SingleMessageSendingRequest(message));

        redisTemplate.opsForValue().set(phoneNumber, code, Duration.ofMinutes(3));
    }

    @Override
    @Transactional
    public boolean verifyAuthCode(String phoneNumber, String code){
        String savedCode = redisTemplate.opsForValue().get(phoneNumber);
        if(savedCode != null && savedCode.equals(code)) {
            redisTemplate.delete(phoneNumber);
            return true;
        }
        return false;
    }

    private String generateAuthCode() {
        Random random =new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
