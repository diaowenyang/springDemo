package be.better.spring.service;

import be.better.spring.core.beans.Service;

@Service
public class TestService {

    public Integer testMethod(int num) {
        return num * 1500;
    }
}
