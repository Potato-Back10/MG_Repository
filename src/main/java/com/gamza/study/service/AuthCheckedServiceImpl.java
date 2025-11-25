package com.gamza.study.service;

import com.gamza.study.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthCheckedServiceImpl implements AuthCheckedService {
    private final AuthRepository authRepository;

    @Override
    public boolean isLoginIdDuplicate(String loginId) {
        return authRepository.existsByLoginId(loginId);
    }
}
