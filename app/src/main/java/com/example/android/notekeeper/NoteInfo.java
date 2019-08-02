package com.example.android.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is about the information of one of the notes, NoteInfo
 * We want to take a NoteInfo and be able to pass it as an extra in an Intent and pass it from one activity to another
 * To do this we will need to make the NoteInfo class Parcelable
 * We are implementing Parcelable methods and interface(describeContents() and writeToParcel() )
 */

public final class NoteInfo implements Parcelable {
    //CourseInfo is class
    private CourseInfo mCourse;
    private String mTitle;
    private String mText;

    public NoteInfo(CourseInfo course, String title, String text) {
        mCourse = course;
        mTitle = title;
        mText = text;
    }

    /*
    The constructor we are using to recreate our object from the parcel
    It has a parameter name source of type Parcel
    We use a private constructor since we might have fields marked as final(private and final go hand in hand)
    We should read the member in the same order as we wrote them
    Reading a Parcelable, we must pass in the class loader information of that type
    */
    private NoteInfo(Parcel source) {
        mCourse = source.readParcelable(CourseInfo.class.getClassLoader());
        mTitle = source.readString();
        mText = source.readString();
    }

    public CourseInfo getCourse() {
        return mCourse;
    }

    public void setCourse(CourseInfo course) {
        mCourse = course;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    private String getCompareKey() {
        return mCourse.getCourseId() + "|" + mTitle + "|" + mText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteInfo that = (NoteInfo) o;

        return getCompareKey().equals(that.getCompareKey());
    }

    @Override
    public int hashCode() {
        return getCompareKey().hashCode();
    }

    @Override
    public String toString() {
        return getCompareKey();
    }

    /*
    describeContents is used to indicate any special behaviour for our Parcelable
    Since we don't have any special needs, we return 0
    */
    @Override
    public int describeContents() {
        return 0;
    }

    /*
    writeToParcel uses the write methods to write the member information for the type instance to the Parcel
    It receives Parcel as a parameter
    dest = destination
    Since mCourse is of type CourseInfo which is a class, we write it differently to the others.
    Flags(ParcelableFlags) is for any special handling and since we have none, we pass in 0
    */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mCourse, 0);
        dest.writeString(mTitle);
        dest.writeString(mText);
    }

    /*
    The static field CREATOR is used to recreate the object from the Parcel
    It must be of type Parcelable.Creator
    The info from this field will then be used to create new instances of our type hence the Parcelable.Creator interface is generic
    We then have to provide our type name as a generic argument.<NoteInfo>
    CREATOR is now able to create instances of type NoteInfo
    We then need to implement a class that can provide the Parcelable.Creator behaviour using an anonymous class(new part)
    */
    public final static Parcelable.Creator<NoteInfo> CREATOR =
            /*
            The anonymous class has two methods; createFromParcel and newArray of type NoteInfo
            createFromParcel creates a new type instance.
            Rather than setting all the values directly under createFromParcel, we'll use a private constructor NoteInfo with a variable source.
            When setting the values, they should be in the same order as in writeToParcel
            newArray creates an array NoteInfo with a variable size as its size
            */
            new Creator<NoteInfo>() {

                @Override
                public NoteInfo createFromParcel(Parcel source) {
                    return new NoteInfo(source);
                }

                @Override
                public NoteInfo[] newArray(int size) {
                    return new NoteInfo[size];
                }
            };//we end an anonymous class with a semicolon since it is a class declaration but used as a statement
}
