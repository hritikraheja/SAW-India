package com.example.saw_india.modalClasses;

import java.io.Serializable;

public class Video implements Serializable {
    String videoLink;
    String thumbnailLink;
    String iconLink;
    String title;
    String organizationName;
    String description;

    public Video(String videoLink, String thumbnailLink, String iconLink, String title, String organizationName, String description) {
        this.videoLink = videoLink;
        this.thumbnailLink = thumbnailLink;
        this.iconLink = iconLink;
        this.title = title;
        this.organizationName = organizationName;
        this.description = description;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public String getIconLink() {
        return iconLink;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoLink='" + videoLink + '\'' +
                ", thumbnailLink='" + thumbnailLink + '\'' +
                ", iconLink='" + iconLink + '\'' +
                ", title='" + title + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", description='" + description + '\'' +
                '}' + "\n";
    }
}
