package com.kyser.demosuite.service.model;

import java.io.Serializable;

public class FeaturedModel implements Serializable {

    private Integer id;
    private String duration;
    private String genre;
    private Integer mid;
    private String rating_name;
    private String synopsisposter;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getRating() {
        return rating_name;
    }

    public void setRating(String rating) {
        this.rating_name = rating;
    }

    public String getSynopsisposter() {
        return synopsisposter;
    }

    public void setSynopsisposter(String synopsisposter) {
        this.synopsisposter = synopsisposter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
