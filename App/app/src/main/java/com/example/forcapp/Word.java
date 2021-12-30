package com.example.forcapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable {
    String title, subtitle, description;

    public Word(String title) {
        this.title = title;
    }

    public Word(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.title = data[0];
    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.title,});
    }
    public static final Creator CREATOR = new Creator() {
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
