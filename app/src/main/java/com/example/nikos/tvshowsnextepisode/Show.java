package com.example.nikos.tvshowsnextepisode;

import java.io.Serializable;

public class Show implements Serializable{

    private String name;
    private String link;
    private String nextEpisode;

    public Show(String name, String link, String nextEpisode) {
        this.name = name;
        this.link = link;
        this.nextEpisode = nextEpisode;
    }

    public Show(String name, String link) {
        this.name = name;
        this.link = link;
        this.nextEpisode = "null";
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getNextEpisode() {
        return nextEpisode;
    }
}
