
package com.example.nizar.quraanapp.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReciterObj {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Server")
    @Expose
    private String server;
    @SerializedName("rewaya")
    @Expose
    private String rewaya;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("letter")
    @Expose
    private String letter;
    @SerializedName("suras")
    @Expose
    private String suras;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRewaya() {
        return rewaya;
    }

    public void setRewaya(String rewaya) {
        this.rewaya = rewaya;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getSuras() {
        return suras;
    }

    public void setSuras(String suras) {
        this.suras = suras;
    }

    @Override
    public String toString() {
        return getName()+getServer()+getCount()+getId()+getLetter()+getRewaya()+getSuras()
                +getCount();
    }
}
