package com.howest.nmct.bob.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.data.Contracts.UserEntry;

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * illyism
 * 21/10/15
 */
public class User implements Parcelable {
    @SerializedName("_id")
    @Expose
    public String Id;

    @SerializedName("facebookID")
    @Expose
    public String facebookID;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("firstName")
    @Expose
    public String firstName;

    @SerializedName("lastName")
    @Expose
    public String lastName;

    @SerializedName("picture")
    @Expose
    public String picture;

    @SerializedName("cover")
    @Expose
    public String cover;

    @SerializedName("link")
    @Expose
    public String link;

    @SerializedName("aboutMe")
    @Expose
    public String aboutMe;

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("carModel")
    @Expose
    public String carModel;

    @SerializedName("carNo")
    @Expose
    public String carNo;

    public User() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(facebookID);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(picture);
        dest.writeString(cover);
        dest.writeString(link);
        dest.writeString(aboutMe);
        dest.writeString(mobile);
        dest.writeString(carModel);
        dest.writeString(carNo);
    }

    public User(Parcel in) {
        Id = in.readString();
        facebookID = in.readString();
        name = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        picture = in.readString();
        cover = in.readString();
        link = in.readString();
        aboutMe = in.readString();
        mobile = in.readString();
        carModel = in.readString();
        carNo = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public User createFromParcel(Parcel in) {
                    return new User(in);
                }

                public User[] newArray(int size) {
                    return new User[size];
                }
            };

    public String getId() {
        return Id;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getCover() {
        return cover;
    }

    public String getPicture() {
        return picture;
    }

    public String getAboutMe() {
        if (aboutMe == null) return "";
        return aboutMe;
    }

    public String getMobile() {
        if (mobile == null || mobile.isEmpty()) return "";
        return PhoneNumberUtils.formatNumber(mobile, "BE");
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public static ContentValues asContentValues(User u) {
        ContentValues values = new ContentValues();
        values.put(UserEntry._ID, u.Id);
        values.put(UserEntry.COLUMN_FACEBOOKID, u.facebookID);
        values.put(UserEntry.COLUMN_NAME, u.name);
        values.put(UserEntry.COLUMN_FIRSTNAME, u.firstName);
        values.put(UserEntry.COLUMN_LASTNAME, u.lastName);
        values.put(UserEntry.COLUMN_PICTURE, u.picture);
        values.put(UserEntry.COLUMN_COVER, u.cover);
        values.put(UserEntry.COLUMN_LINK, u.link);
        values.put(UserEntry.COLUMN_ABOUTME, u.aboutMe);
        values.put(UserEntry.COLUMN_MOBILE, u.mobile);
        values.put(UserEntry.COLUMN_CAR_MODEL, u.carModel);
        values.put(UserEntry.COLUMN_CAR_NO, u.carNo );
        return values;
    }

    public static User createFromCursor(Cursor data) {
        User newUser = new User();

        int indexId = data.getColumnIndex(UserEntry._ID);
        int indexFacebookID = data.getColumnIndex(UserEntry.COLUMN_FACEBOOKID);
        int indexName = data.getColumnIndex(UserEntry.COLUMN_NAME);
        int indexFirstName = data.getColumnIndex(UserEntry.COLUMN_FIRSTNAME);
        int indexLastName = data.getColumnIndex(UserEntry.COLUMN_LASTNAME);
        int indexPicture = data.getColumnIndex(UserEntry.COLUMN_PICTURE);
        int indexCover = data.getColumnIndex(UserEntry.COLUMN_COVER);
        int indexLink = data.getColumnIndex(UserEntry.COLUMN_LINK);
        int indexAboutMe = data.getColumnIndex(UserEntry.COLUMN_ABOUTME);
        int indexMobile = data.getColumnIndex(UserEntry.COLUMN_MOBILE);
        int indexCarModel = data.getColumnIndex(UserEntry.COLUMN_CAR_MODEL);
        int indexCarNo = data.getColumnIndex(UserEntry.COLUMN_CAR_NO);

        newUser.Id = data.getString(indexId);
        newUser.facebookID = data.getString(indexFacebookID);
        newUser.name = data.getString(indexName);
        newUser.firstName = data.getString(indexFirstName);
        newUser.lastName = data.getString(indexLastName);
        newUser.picture = data.getString(indexPicture);
        newUser.cover = data.getString(indexCover);
        newUser.link = data.getString(indexLink);
        newUser.aboutMe = data.getString(indexAboutMe);
        newUser.mobile = data.getString(indexMobile);
        newUser.carModel = data.getString(indexCarModel);
        newUser.carNo = data.getString(indexCarNo);

        return newUser;
    }

    public static ContentValues[] asContentValues(LinkedHashSet<Ride> rides) {
        HashMap<String, ContentValues> values = new HashMap<>();
        for (Ride r : rides) {
            ContentValues driverValues = asContentValues(r.driver);
            if (driverValues != null) values.put(r.driver.Id, driverValues);
            for (User u : r.approvedList) {
                ContentValues listValues = asContentValues(u);
                if (listValues != null) values.put(u.Id, listValues);
            }
            for (User u : r.requestsList) {
                ContentValues listValues = asContentValues(u);
                if (listValues != null) values.put(u.Id, listValues);
            }
        }
        return values.values().toArray(new ContentValues[values.size()]);
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCarModel() {
        if (carModel == null) return "";
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNo() {
        if (carNo == null) return "";
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
}
