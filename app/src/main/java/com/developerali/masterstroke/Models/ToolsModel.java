package com.developerali.masterstroke.Models;

import android.graphics.drawable.Drawable;

public class ToolsModel {

    String name;
    Drawable drawable;

    public ToolsModel() {
    }

    public ToolsModel(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
