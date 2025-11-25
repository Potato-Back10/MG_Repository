package com.gamza.study.service;

public interface MessageService {
    void sendAuthCode(String phoneNumber);

    boolean verifyAuthCode(String phoneNumber, String code);
}
