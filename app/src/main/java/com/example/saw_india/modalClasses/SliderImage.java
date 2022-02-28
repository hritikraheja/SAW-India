package com.example.saw_india.modalClasses;

import androidx.annotation.NonNull;

public class SliderImage {
    String imageUrl;

    public SliderImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return imageUrl;
    }
}
