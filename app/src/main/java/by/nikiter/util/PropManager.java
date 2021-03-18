package by.nikiter.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class PropManager {

    public static String getLabel(String label) {
        return ResourceBundle.getBundle("labels").getString(label);
    }

    public static String getLabel(String label, Locale locale) {
        return ResourceBundle.getBundle("labels", locale).getString(label);
    }
}
