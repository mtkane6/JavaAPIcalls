package com.company;

import com.google.gson.annotations.SerializedName;

public class NewsSource {
    @SerializedName("id")
    private String ID;
    @SerializedName("name")
    private String Name;

    public NewsSource(String ID, String Name) {
        this.ID = ID;
        this.Name = Name;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.Name;
    }
}
