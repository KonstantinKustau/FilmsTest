package com.example.ibanez_xiphos.itrextest.adapter;

public class RecyclerItem {

    private long id;
    private String image;
    private String name;
    private String nameEng;
    private String premiere;
    private String description;

    public RecyclerItem(long id, String image, String name, String nameEng, String premiere, String description) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.nameEng = nameEng;
        this.premiere = premiere;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getNameEng() {
        return nameEng;
    }

    public String getPremiere() {
        return premiere;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }
}
