package com.example.elias.rememberlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by elias on 06-10-2016.
 */

public class Product implements Parcelable {

    private String name;
    private int quantity;

    public Product(Parcel product){
        name = product.readString();
        quantity = product.readInt();
    }

    public Product(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return name + ": " + quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
