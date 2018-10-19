
package com.example;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("val")
    @Expose
    private List<Val> val = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Val> getVal() {
        return val;
    }

    public void setVal(List<Val> val) {
        this.val = val;
    }

}
