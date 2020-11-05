package com.example.winko.DB.models;

public class WineModel {
    private String name;
    private float rating;
    private byte[] photo;
    private int ID;
    public WineModel(int id,String name, float rating, byte[] photo){
        this.name = name;
        this.rating = rating;
        this.photo = photo;
        this.ID = id;
    }
    public WineModel(String name, float rating, byte[] photo){
        this.name = name;
        this.rating = rating;
        this.photo = photo;
    }
    public int getID() { return ID;}
    public String getName(){
        return name;
    }
    public byte[] getPhotoAsByteArray(){
        return photo;
    }
    public float getRating(){
        return rating;
    }
}

