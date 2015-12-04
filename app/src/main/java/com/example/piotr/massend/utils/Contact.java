package com.example.piotr.massend.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {

    int id;
    String name;
    String phone;
    String group;
    boolean isChecked = false;

    public Contact(int id, String name, String phone, String group) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.group = group;
    }

    public Contact(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.phone = data[2];
        this.group = data[3];

    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return name + ", tel: " + phone + ", z grupy: " + group;
    }

    /* returns state AFTER operation */
    public boolean checkOrUncheck() {
        isChecked = !isChecked;
        return isChecked;
    }

    public void check() {
        isChecked = true;
    }

    public void uncheck() {
        isChecked = false;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{Integer.toString(id), name, phone, group});
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
