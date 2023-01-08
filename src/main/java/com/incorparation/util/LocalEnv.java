package com.incorparation.util;

import org.springframework.stereotype.Component;

@Component
public class LocalEnv implements KeyManager {

    @Override
    public String getDbUrl() {
        return "jdbc:mysql://localhost:3306/storage_house";
    }

    @Override
    public String getUserName() {
        return "root";
    }

    @Override
    public String getPassword() {
        return "11111Aa!";
    }

    @Override
    public int getMaxPollSize() {
        return 5;
    }

    @Override
    public long getMaxLifeTime() {
        return 900000;
    }
}
