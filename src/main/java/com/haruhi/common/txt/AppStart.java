package com.haruhi.common.txt;

import com.haruhi.common.txt.app.Context;
import com.haruhi.common.txt.app.StandardCharset;

import java.nio.charset.Charset;

/**
 * @author cppno1
 */
public class AppStart {
    public static void main(String[] args) {
        for (String s : StandardCharset.ALL_CHARSET) {
            String[] strings = s.split(" ");
            Charset charset = Charset.forName(strings[strings.length - 1]);
        }
        CommonTxtApplication.main(args);
    }
}
