package com.haruhi.common.txt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author cppno1
 */
public class AppStart {
    static Log log = LogFactory.getLog(AppStart.class);
    public static void main(String[] args) {
        log.debug("test");
        CommonTxtApplication.main(args);
    }
}
