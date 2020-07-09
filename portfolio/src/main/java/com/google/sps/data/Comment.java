package com.google.sps.data;


public class Comment { 
    long timestamp;
    long id;
    String name;
    String text;

    // Constructor Declaration of Class 
    public Comment(long timestamp, String name, String text, 
                   long id) { 
        this.timestamp = timestamp; 
        this.name = name; 
        this.text = text; 
        this.id = id; 
    }  
  
} 