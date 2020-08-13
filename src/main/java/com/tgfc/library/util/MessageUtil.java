package com.tgfc.library.util;


import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

@Component
public class MessageUtil {

    private static ResourceBundle res = ResourceBundle.getBundle("messages");

    public static String getMessage(String property) {
        return res.getString(property);
    }

}
