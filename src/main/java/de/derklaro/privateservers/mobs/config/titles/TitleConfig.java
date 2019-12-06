package de.derklaro.privateservers.mobs.config.titles;

import java.util.List;

public final class TitleConfig {

    private List<Title> titles;

    public TitleConfig(List<Title> titles) {
        this.titles = titles;
    }

    public List<Title> getTitles() {
        return this.titles;
    }
}
