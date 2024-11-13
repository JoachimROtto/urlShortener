package de.gfed.urlshortener.model;

public class FormData {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FormData(String text){
        this.text=text;
    }
}
