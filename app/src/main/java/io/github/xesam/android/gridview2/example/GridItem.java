package io.github.xesam.android.gridview2.example;

public class GridItem {
    private int imageResId;
    private String title;
    private String subtitle;

    public GridItem(int imageResId, String title, String subtitle) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}