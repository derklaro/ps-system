package de.derklaro.privateservers.mobs.config.titles;

public final class Title {

    private String title;

    private String subTitle;

    public Title(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }
}
