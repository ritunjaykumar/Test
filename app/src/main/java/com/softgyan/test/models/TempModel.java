package com.softgyan.test.models;

public class TempModel {
    private final int id;
    private final String imageName;
    private final String imageUri;

    public TempModel(int id, String imageName, String imageUri) {
        this.id = id;
        this.imageName = imageName;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public String toString() {
        return "TempModel{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
