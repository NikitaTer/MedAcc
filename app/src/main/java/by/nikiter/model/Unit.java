package by.nikiter.model;

import by.nikiter.App;

import java.util.Locale;

public enum Unit {

    PIECE("Piece","Pc", "Штука", "Шт"),
    LITER("Liter","L","Литр","л"),
    MILLILITER("Milliliter","ml","Миллилит","мл"),
    KILOGRAM("Kilogram","kg","Килограмм","кг"),
    GRAM("Gram","g","Грамм","г");

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


    @Override
    public String toString() {
        return getNameShort();
    }
}
