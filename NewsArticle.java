package com.company;

import com.google.gson.annotations.SerializedName;

import javax.xml.transform.Source;
import java.util.ArrayList;

public class NewsArticle {
    @SerializedName("articles")
    public ArrayList< Article > articles;


    public NewsArticle(ArrayList<Article> articles) {
        this.articles = articles;
    }


}
