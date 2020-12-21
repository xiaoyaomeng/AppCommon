package com.panghu.uikit.utils;


/**
 * Created by panghu on 9/11/17.
 */

public interface ILogPrinter {
    int VERBOSE = 2;
    int DEBUG = 3;
    int INFO = 4;
    int WARN = 5;
    int ERROR = 6;

    void print(int level, String tag, String message);
}
