package com.haruhi.common.txt;

import java.io.UnsupportedEncodingException;

/**
 * @author cppno1
 */
public class AppStart {
    public static void main(String[] args) {
        byte[] bytes = new byte[]{1,2,3};
        try {
            String[] charsetArr = new String[]{
                    "UTF-8 utf8",
                    "UTF-16LE utf16le",
                    "UTF-16BE utf16be",
                    "Western(Windows 1252) windows1252",
                    "Western(s0 8859-1) iso88591",
                    "Western(1SO 8859-3) iso88593",
                    "Western(1S0 8859- 15) iso885915",
                    "Western(Mac Roman) macroman",
                    "DOS(CP 437) cp437",
                    "Arabic(Windows 1256) windows1256",
                    "Arabic(so 8859-6) iso88596",
                    "Baltic(Windows 1257) windows1257",
                    "Baltic(1SO 8859-4) iso88594",
                    "Celtic(ISO 8859-14) iso885914",
                    "Central European(Windows 1250) windows1250",
                    "Central European(ISO 8859-2) iso88592",
                    "Central European(CP 852) cp852",
                    "Cyrillic(Windows 1251) windows1251",
                    "Cyrillic(CP 866) cp866",
                    "Cyrillic(1s0 8859-5) iso88595",
                    "Cyrillic(KO18-R) koi8r",
                    "Cyrillic(KO18-U) koi8u",
                    "Estonian(1so 8859-13) iso885913",
                    "Greek(Windows 1253) windows1253",
                    "Greek(1SO 8859-7) iso88597",
                    "Hebrew(Windows 1255) windows1255",
                    "Hebrew1so 8859-8) iso88598",
                    "Nordic(1so 8859-10) iso885910",
                    "Romanian(1S0 8859-16) iso885916",
                    "Turkish(Windows 1254) windows1254",
                    "Turkish(lSO 8859-9) iso88599",
                    "Vietnamese(Windows 1258) windows1258",
                    "Simplified Chinese(GBK) gbk",
                    "Simplified Chinese(GB18030) gb18030",
                    "Traditional Chinese(Big5) cp950",
                    "Traditional Chinese(Big5-HKSCS) big5hkscs",
                    "Japanese(Shift JIS) shiftjis",
                    "Japanese(EUC-JP) eucjp",
                    "Korean(EUC-KR) euckr",
                    "Thai(Windows 874) windows874",
                    "Latin/Thai(1S0 8859-11) iso885911",
                    "Cyrillic(KO18-RU) koi8ru",
                    "Tajik(KO18-T) koi8t",
                    "Simplified Chinese(GB 2312) gb2312",
                    "Nordic DOS(CP 865) cp865",
                    "Western European DOS(CP 850) cp850",
            };
            String str = new String(bytes, "CP866");
            System.out.println(str);
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CommonTxtApplication.main(args);
    }
}
