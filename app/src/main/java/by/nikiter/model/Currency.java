package by.nikiter.model;

public enum Currency {

    USD("USD","$"),
    BYN("BYN","BYN");

    private final String name;
    private final String nameShort;

    Currency(String name, String nameShort) {
        this.name = name;
        this.nameShort = nameShort;
    }

    public String getName() {
        return name;
    }

    public String getNameShort() {
        return nameShort;
    }


    @Override
    public String toString() {
        return getName();
    }
}
