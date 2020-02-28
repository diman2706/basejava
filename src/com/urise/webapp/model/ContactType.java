package com.urise.webapp.model;

public enum ContactType {
    PHONENUMBER("Тел."),
    SKYPE("Skype"),
    MAIL("Почта"),
    LINCKEDLN("Профиль LinkedIn"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
