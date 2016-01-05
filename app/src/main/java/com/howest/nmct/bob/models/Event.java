package com.howest.nmct.bob.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.howest.nmct.bob.data.Contracts.EventEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * Nick on 28/10/2015.
 */
public class Event implements Parcelable {
    public static final int VISIBILE = 0;
    public static final int HIDDEN = 1;

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("start_time")
    @Expose
    public String startTime;

    @SerializedName("updated_time")
    @Expose
    public String updatedTime;

    @SerializedName("end_time")
    @Expose
    public String endTime;

    @SerializedName("cover")
    @Expose
    public String cover;

    @SerializedName("picture")
    @Expose
    public String picture;

    @SerializedName("attending_count")
    @Expose
    public int attendingCount;

    @SerializedName("declined_count")
    @Expose
    public int declinedCount;

    @SerializedName("interested_count")
    @Expose
    public int interestedCount;

    @SerializedName("noreply_count")
    @Expose
    public int noreplyCount;

    @SerializedName("owner")
    @Expose
    public Owner owner;

    @SerializedName("can_guests_invite")
    @Expose
    public boolean canGuestsInvite;

    @SerializedName("guest_list_enabled")
    @Expose
    public boolean guestListEnabled;

    @SerializedName("rsvp_status")
    @Expose
    public String rsvpStatus;

    @SerializedName("place")
    @Expose
    public Place place;

    /**
     * Event
     * @param noreplyCount Users with no reply
     * @param attendingCount Users attending
     * @param endTime End time of the time
     * @param id Id of the event
     * @param startTime Start time of the event
     * @param picture Picture of the event
     * @param interestedCount Users interested
     * @param cover Cover photo
     * @param description The long form description
     * @param name The name of the event
     * @param owner The owner of the event
     * @param rsvpStatus RSVP status
     * @param canGuestsInvite If guests can invite
     * @param declinedCount Users not going
     * @param place Place of the event
     * @param guestListEnabled If guests list is visible
     * @param updatedTime Time the event was updated
     */
    public Event(String id, String name, String description, String startTime, String updatedTime, String endTime, String cover, String picture, int attendingCount, int declinedCount, int interestedCount, int noreplyCount, Owner owner, boolean canGuestsInvite, boolean guestListEnabled, String rsvpStatus, Place place) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.updatedTime = updatedTime;
        this.endTime = endTime;
        this.cover = cover;
        this.picture = picture;
        this.attendingCount = attendingCount;
        this.declinedCount = declinedCount;
        this.interestedCount = interestedCount;
        this.noreplyCount = noreplyCount;
        this.owner = owner;
        this.canGuestsInvite = canGuestsInvite;
        this.guestListEnabled = guestListEnabled;
        this.rsvpStatus = rsvpStatus;
        this.place = place;
    }

    public Event() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public static Date parseDate(String dateString) {
        if (dateString == null) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDate(String datePattern, Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.US);
        return sdf.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(startTime);
        dest.writeString(updatedTime);
        dest.writeString(endTime);
        dest.writeString(cover);
        dest.writeString(picture);
        dest.writeInt(attendingCount);
        dest.writeInt(declinedCount);
        dest.writeInt(interestedCount);
        dest.writeInt(noreplyCount);
        dest.writeParcelable(owner, flags);
        dest.writeByte((byte) (canGuestsInvite ? 1 : 0));
        dest.writeByte((byte) (guestListEnabled ? 1 : 0));
        dest.writeString(rsvpStatus);
        dest.writeParcelable(place, flags);
    }

    private Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        startTime = in.readString();
        updatedTime = in.readString();
        endTime = in.readString();
        cover = in.readString();
        picture = in.readString();
        attendingCount = in.readInt();
        declinedCount = in.readInt();
        interestedCount = in.readInt();
        noreplyCount = in.readInt();
        owner = in.readParcelable(Owner.class.getClassLoader());
        canGuestsInvite = in.readByte() != 0;
        guestListEnabled = in.readByte() != 0;
        rsvpStatus = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Event createFromParcel(Parcel in) {
                    return new Event(in);
                }

                public Event[] newArray(int size) {
                    return new Event[size];
                }
            };

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        Event otherEvent = (Event) o;
        return otherEvent != null && this.getId().equals(otherEvent.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public static ContentValues[] asContentValues(LinkedHashSet objects) {
        ContentValues[] values = new ContentValues[objects.size()];
        int i = 0;
        for (Iterator iter = objects.iterator(); iter.hasNext(); i++) {
            Object o = iter.next();
            if (o instanceof Event) {
                Event e = (Event) o;
                values[i] = asContentValues(e);
            }
            if (o instanceof Ride) {
                Ride r = (Ride) o;
                values[i] = asContentValues(r.event);
            }
        }
        return values;
    }

    public static ContentValues asContentValues(Event e) {
        ContentValues values = new ContentValues();
        values.put(EventEntry._ID, e.id);
        values.put(EventEntry.COLUMN_NAME, e.name);
        values.put(EventEntry.COLUMN_DESCRIPTION, e.description);
        values.put(EventEntry.COLUMN_START_TIME, e.startTime);
        values.put(EventEntry.COLUMN_UPDATED_TIME, e.updatedTime);
        values.put(EventEntry.COLUMN_END_TIME, e.endTime);
        values.put(EventEntry.COLUMN_COVER, e.cover);
        values.put(EventEntry.COLUMN_PICTURE, e.picture);
        values.put(EventEntry.COLUMN_ATTENDING_COUNT, e.attendingCount);
        values.put(EventEntry.COLUMN_DECLINED_COUNT, e.declinedCount);
        values.put(EventEntry.COLUMN_INTERESTED_COUNT, e.interestedCount);
        values.put(EventEntry.COLUMN_NOREPLY_COUNT, e.noreplyCount);
        values.put(EventEntry.COLUMN_OWNER, e.owner.name);
        values.put(EventEntry.COLUMN_CAN_GUESTS_INVITE, e.canGuestsInvite);
        values.put(EventEntry.COLUMN_GUEST_LIST_ENABLED, e.guestListEnabled);
        values.put(EventEntry.COLUMN_RSVP_STATUS, e.rsvpStatus);
        if (e.place != null)
            values.put(EventEntry.COLUMN_PLACE_ID, e.place.id);
        return values;
    }
}
