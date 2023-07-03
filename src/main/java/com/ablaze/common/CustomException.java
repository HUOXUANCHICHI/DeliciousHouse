package com.ablaze.common;

/**
 * 自定义业务异常类
 *
 * @author ablaze
 * @Date: 2023/07/03/21:25
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
