package com.example.mithraapplication.ModelClasses;

import java.util.ArrayList;

public class VideoModules {
    private String videoModuleName;
    private ArrayList<SingleVideo> singleVideoArrayList;

    public String getVideoModuleName() {
        return videoModuleName;
    }

    public void setVideoModuleName(String videoModuleName) {
        this.videoModuleName = videoModuleName;
    }

    public ArrayList<SingleVideo> getSingleVideoArrayList() {
        return singleVideoArrayList;
    }

    public void setSingleVideoArrayList(ArrayList<SingleVideo> singleVideoArrayList) {
        this.singleVideoArrayList = singleVideoArrayList;
    }
}
