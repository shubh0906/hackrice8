package com.shopiholik.app.model;

/**
 * @author agrawroh
 * @version v1.0
 */
public class MainCard {
    private String name;
    private String description;
    private int thumbnail;

    /**
     * Default Constructor.
     */
    public MainCard() {
        /* Do Nothing */
    }

    /**
     * Copy Constructor.
     *
     * @param name        Name
     * @param description Description
     * @param thumbnail   Thumbnail
     */
    public MainCard(final String name, String description, int thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(int numOfSongs) {
        this.description = description;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
