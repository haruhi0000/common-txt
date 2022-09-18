package com.haruhi.common.txt;

import com.haruhi.common.txt.app.Context;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author haruhi0000
 */
public class AppStart {
    public static void main(String[] args) {
        Preferences preferences = Preferences.userRoot().node(Context.appName);
        Context.initSourceFileDir = new File(preferences.get("initSourceFileDir", System.getProperty("user.home")));
        CommonTxtApplication.main(args);
    }
}
