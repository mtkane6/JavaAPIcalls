package com.company;

import com.google.gson.annotations.SerializedName;

import javax.xml.transform.Source;

public class Article {
    @SerializedName("source")
    private NewsSource source;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;

    public Article(NewsSource source, String author, String content, String title) {
        this.source = source;
        this.author = author;
        this.content = content;
        this.title = title;
    }
    public NewsSource getSource() {
        return this.source;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getContent() {
        return this.content;
    }
    public String getTitle() {
        return this.title;
    }
}
