package com.example.mithraapplication.ModelClasses;

import java.io.Serializable;

public class SingleVideo implements Serializable {
    private String videoName;
    private String videoDescription;
    private String videoStatus;
    private String videoPath;
    private boolean isVideoPlayed = false;

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(String videoStatus) {
        this.videoStatus = videoStatus;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isVideoPlayed() {
        return isVideoPlayed;
    }

    public void setVideoPlayed(boolean videoPlayed) {
        isVideoPlayed = videoPlayed;
    }
}
