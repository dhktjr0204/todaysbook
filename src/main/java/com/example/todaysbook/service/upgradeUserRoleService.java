package com.example.todaysbook.service;


import com.example.todaysbook.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class upgradeUserRoleService {

    private final UserMapper userMapper;

    //매달 1일 정각에 업데이트
    @Scheduled(cron = "0 0 0 1 * *")
    public void upgradeUserRole(){
        userMapper.updateUserRole();
    }
}
