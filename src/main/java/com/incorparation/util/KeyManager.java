package com.incorparation.util;

public interface KeyManager {
    String getDbUrl();
    String getUserName();
    String getPassword();
    int getMaxPollSize();
    long getMaxLifeTime();
}
