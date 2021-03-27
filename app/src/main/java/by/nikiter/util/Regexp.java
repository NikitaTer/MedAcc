package by.nikiter.util;

public class Regexp {

    public static final String DECIMAL = "^(?:[1-9][0-9]{0,10})$";
    public static final String DOUBLE = "^(?:[1-9][0-9]{0,10}\\.[0-9]{1,10})|(?:0\\.[0-9]{1,10})|(?:[1-9][0-9]{0,10})$";
    public static final String DOUBLE_SPACE = "\\s{2,}";
}
