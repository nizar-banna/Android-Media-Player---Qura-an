
package com.example.nizar.quraanapp.modal;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rewaya {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("val")
    @Expose
    private ArrayList<ReciterObj> val = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ReciterObj> getVal() {
        return val;
    }

    public void setVal(ArrayList<ReciterObj> val) {
        this.val = val;
    }


    @Override
    public String toString() {
        return getVal()+"";
    }
}
