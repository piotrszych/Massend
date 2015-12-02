package com.example.piotr.massend.utils;

public class Template {

    String name;
    String content;

    public Template(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Template " + name + ": \"" + content + "\"";
    }
}
