package com.example.todaysbook.constant;

import java.util.regex.Pattern;

public class Constant {

    public static int REVIEW_CONTENT_MAX_LENGTH = 500;

    public static int NICKNAME_MIN_LENGTH = 2;

    public static int NICKNAME_MAX_LENGTH = 8;

    public static Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");

    public static Pattern DEFAULT_ADDRESS_PATTERN = Pattern.compile(", ");
    public static Pattern ZIPCODE_PATTERN = Pattern.compile("^\\d{5}$");

    public static int BOOK_TITLE_MAX_LENGTH = 200;
    public static int AUTHOR_MAX_LENGTH = 200;
    public static int PUBLISHER_MAX_LENGTH = 20;
    public static int DESCRIPTION_MAX_LENGTH = 300;

    public static int LIST_TITLE_MAX_LENGTH = 30;
    public static int LIST_SIZE = 10;
}
