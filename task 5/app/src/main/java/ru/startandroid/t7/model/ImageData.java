package ru.startandroid.t7.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {
    public int id;
    public String URL;
    public String description;

    public ImageData(int id, String _URL, String _description) {
        this.id = id;
        URL = _URL;
        description = _description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(URL);
        dest.writeString(description);
    }

    private ImageData(Parcel parcel) {
        id = parcel.readInt();
        URL = parcel.readString();
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
