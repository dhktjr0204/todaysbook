package com.example.todaysbook.util;

import java.util.HashMap;

public class Pagination {

    private static final int VISIBLE_PAGE = 5;
    static public HashMap<String, Integer> calculatePage(int currentPage, int totalPage) {

        HashMap<String, Integer> result = new HashMap<>();

        int startPage = (currentPage / VISIBLE_PAGE) * VISIBLE_PAGE + 1;
        int endPage = Math.min(totalPage, (currentPage / VISIBLE_PAGE) * VISIBLE_PAGE + VISIBLE_PAGE);

        result.put("startPage", startPage);
        result.put("endPage", endPage);

        return result;
    }
}
