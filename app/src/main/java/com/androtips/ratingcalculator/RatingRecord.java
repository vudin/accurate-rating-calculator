package com.androtips.ratingcalculator;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingRecord implements Serializable, Parcelable {

    private static final long serialVersionUID = 5648086117227236918L;

    private String fiveStars;
    private String fourStars;
    private String threeStars;
    private String twoStars;
    private String oneStar;

    private double average;

    private String date;

    public RatingRecord(String fiveStars, String fourStars, String threeStars, String twoStars, String oneStar, double average, String date) {
        super();
        this.fiveStars = fiveStars;
        this.fourStars = fourStars;
        this.threeStars = threeStars;
        this.twoStars = twoStars;
        this.oneStar = oneStar;
        this.average = average;
        this.date = date;
    }

    public RatingRecord(Parcel in) {
        fiveStars = in.readString();
        fourStars = in.readString();
        threeStars = in.readString();
        twoStars = in.readString();
        oneStar = in.readString();
        average = in.readDouble();
        date = in.readString();
    }

    public String getFiveStars() {
        return fiveStars;
    }

    public String getFourStars() {
        return fourStars;
    }

    public String getThreeStars() {
        return threeStars;
    }

    public String getTwoStars() {
        return twoStars;
    }

    public String getOneStar() {
        return oneStar;
    }

    public double getAverage() {
        return average;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fiveStars);
        dest.writeString(fourStars);
        dest.writeString(threeStars);
        dest.writeString(twoStars);
        dest.writeString(oneStar);
        dest.writeDouble(average);
        dest.writeString(date);
    }

    /**
     * @see Creator
     */
    public static final Parcelable.Creator<RatingRecord> CREATOR = new Parcelable.Creator<RatingRecord>() {

        public RatingRecord createFromParcel(final Parcel in) {
            return new RatingRecord(in);
        }

        public RatingRecord[] newArray(int size) {
            return new RatingRecord[size];
        }
    };

}
