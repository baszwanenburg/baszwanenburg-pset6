package com.example.bas.pset6;

public class MusicClass {
    private String album;
    private String rank;
    private String track;
    private String artist;
    private String year;

    public MusicClass(String album, String rank, String track, String artist, String year) {
        this.album  = album;
        this.rank   = rank;
        this.track  = track;
        this.artist = artist;
        this.year   = year;
    }

    // Default constructor for FireBase
    public MusicClass() {}

    String getImageURL() {
        return album;
    }

    String getRank() {
        return rank;
    }

    String getTrack() {
        return track;
    }

    String getArtist() {
        return artist;
    }

    String getYear() {
        return year;
    }
}
