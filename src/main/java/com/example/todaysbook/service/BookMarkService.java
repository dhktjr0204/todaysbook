package com.example.todaysbook.service;

public interface BookMarkService {
    void addMark(Long userId, Long listId);
    void cancelMark(Long userId, Long listId);
}
