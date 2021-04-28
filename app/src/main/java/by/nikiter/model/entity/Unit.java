package by.nikiter.model.entity;

import by.nikiter.App;

import java.util.Locale;

public enum Unit {

    PIECE("Piece","Pc", "Штука", "Шт"),

    LITER("Liter","L","Литр","л"),
    MILLILITER("Milliliter","ml","Миллилит","мл"),

    KILOGRAM("Kilogram","kg","Килограмм","кг"),
    GRAM("Gram","g","Грамм","г"),
    MILLIGRAM("Milligram","mg","Миллиграмм","мг");

    private final String name;
    private final String nameShort;
    private final String nameRu;
    private final String nameShortRu;

    Unit(String name, String nameShort, String nameRu, String nameShortRu) {
        this.name = name;
        this.nameShort = nameShort;
        this.nameRu = nameRu;
        this.nameShortRu = nameShortRu;
    }

    public String getName() {
        return App.getLocale().toLanguageTag().equals("ru") ? nameRu : name;
    }

    public String getNameShort() {
        return App.getLocale().toLanguageTag().equals("ru") ? nameShortRu : nameShort;
    }

    public static Unit[] getGramsUnits() {
        Unit[] units = new Unit[3];
        units[0] = MILLIGRAM;
        units[1] = GRAM;
        units[2] = KILOGRAM;

        return units;
    }

    public static Unit[] getLitersUnits() {
        Unit[] units = new Unit[2];
        units[0] = MILLILITER;
        units[1] = LITER;

        return units;
    }

    @Override
    public String toString() {
        return getNameShort();
    }
}
