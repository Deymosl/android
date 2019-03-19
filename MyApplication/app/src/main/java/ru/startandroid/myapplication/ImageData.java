package ru.startandroid.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {
    int id;
    String url;
    String description;

    ImageData(int id, String url, String description) {
        this.id = id;
        this.url = url;
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeString(description);
    }

    private ImageData(Parcel parcel) {
        id = parcel.readInt();
        url = parcel.readString();
        description = parcel.readString();
    }

    public static final Parcelable.Creator<ImageData> CREATOR = new Parcelable.Creator<ImageData>() {
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };
}
