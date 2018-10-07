package com.finsTcp;

import com.finsTcp.language.DefaultLanguage;
import com.finsTcp.language.English;
import java.util.Locale;

public class StringResources {

    static {
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().startsWith("zh")) {
            SetLanguageChinese();
        } else {
            SeteLanguageEnglish();
        }
    }


    public static DefaultLanguage Language = new DefaultLanguage();


    public static void SetLanguageChinese() {
        Language = new DefaultLanguage();
    }


    public static void SeteLanguageEnglish() {
        Language = new English();
    }

}
