package com.example.amrez.bookapp;

/**
 * Created by amrez on 10/6/2017.
 */

public class B_object {

    private final String name;
    private final String author;
    private final String titleInfo;
    private final String previewLink;

    public B_object(String Name, String Author, String Title_info, String previewLink) {
        this.name = Name;
        this.author = Author;
        this.titleInfo = Title_info;
        this.previewLink = previewLink;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle_info() {
        return titleInfo;
    }

    public String getPreviewLInk() {
        return previewLink;
    }
}