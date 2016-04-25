package com.thehp.peek;

/**
 * Created by nirav on 05/10/15.
 */
public class Data {


    private String name;
    public String title;
    public int type;
    private String thumbPath;
    private String imagePath;

    public Data(String imagePath,String name,int type) {
        this.imagePath = imagePath;
        this.name = name;
        this.type=type;
        this.thumbPath=imagePath;
        this.title=name;
    }


    public Data(String imagePath,String thumbPath,String name,String title,int type) {
        this.imagePath = imagePath;
        this.name = name;
        this.type=type;
        this.thumbPath=thumbPath;
        this.title=title;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

}
