package com.kyser.demosuite.service.model;

public class TrackModel {

    private Integer id;
    private Integer aggregateParentmid;
    private String artist;
    private String duration;
    private String mediaType;
    private Integer mid;
    private Integer rating;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAggregateParentmid() {
        return aggregateParentmid;
    }

    public void setAggregateParentmid(Integer aggregateParentmid) {
        this.aggregateParentmid = aggregateParentmid;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
