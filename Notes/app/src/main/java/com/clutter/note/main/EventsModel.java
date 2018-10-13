package com.clutter.note.main;

/**
 * Created by csimcik on 11/21/2017.
 */
public class EventsModel {
    private String eventsID,type, url, thumb;
    public String getEventsID() {
        return eventsID;
    }
    public void setEventsID(String ID) {
        this.eventsID = ID;
    }
    public String getType(){return type;}
    public void setType(String type) {this.type = type;}
    public String getUrl(){return url;}
    public String getThumb(){return thumb;}
    public void setUrl(String url){this.url = url;}
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}

