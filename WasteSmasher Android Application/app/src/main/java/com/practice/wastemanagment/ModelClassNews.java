package com.practice.wastemanagment;

public class ModelClassNews {
    private String title,description,link, image_url, pubDate;

    public ModelClassNews(String creator, String title, String description, String link, String image_url, String pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image_url = image_url;
        this.pubDate = pubDate;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
