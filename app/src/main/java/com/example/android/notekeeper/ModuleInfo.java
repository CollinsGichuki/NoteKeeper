package com.example.android.notekeeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is about the information on the modules, ModuleInfo
 * ModuleInfo is a reference in NoteInfo which is a parcelable hence ModuleInfo should be a Parcelable too
 * We are implementing Parcelable methods and interface(describeContents() and writeToParcel() )
 */

public final class ModuleInfo implements Parcelable {
    private final String mModuleId;
    private final String mTitle;
    private boolean mIsComplete = false;

    public ModuleInfo(String moduleId, String title) {
        this(moduleId, title, false);
    }

    public ModuleInfo(String moduleId, String title, boolean isComplete) {
        mModuleId = moduleId;
        mTitle = title;
        mIsComplete = isComplete;
    }

    /*
   The constructor we are using to recreate our object from the parcel
   It has a parameter name source of type Parcel
   We use a private constructor since we might have fields marked as final(private and final go hand in hand)
   We should read the member in the same order as we wrote them
   Reading a Parcelable, we must pass in the class loader information of that type
   */
    private ModuleInfo(Parcel source) {
        mModuleId = source.readString();
        mTitle = source.readString();
        mIsComplete = source.readByte() == 1;
    }

    public String getModuleId() {
        return mModuleId;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public void setComplete(boolean complete) {
        mIsComplete = complete;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleInfo that = (ModuleInfo) o;

        return mModuleId.equals(that.mModuleId);
    }

    @Override
    public int hashCode() {
        return mModuleId.hashCode();
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
   Flags is for any special handling and since we have none, we pass in 0 or nothing
   */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mModuleId);
        dest.writeString(mTitle);
        dest.writeByte((byte)(mIsComplete ? 1 : 0));
    }

    /*
    The static field CREATOR is used to recreate the object from the Parcel
    It must be of type Parcelable.Creator
    The info from this field will then be used to create new instances of our type hence the Parcelable.Creator interface is generic
    We then have to provide our type name as a generic argument.<ModuleInfo>
    CREATOR is now able to create instances of type ModuleInfo
    We then need to implement a class that can provide the Parcelable.Creator behaviour using an anonymous class(new part)
    */
    public static final Creator<ModuleInfo> CREATOR =
            /*
            The anonymous class has two methods; createFromParcel and newArray of type ModuleInfo
            createFromParcel creates a new type instance.
            Rather than setting all the values directly under createFromParcel, we'll use a private constructor ModuleInfo with a variable source.
            When setting the values, they should be in the same order as in writeToParcel
            newArray creates an array ModuleInfo with a variable size as its size
            */
            new Creator<ModuleInfo>() {

                @Override
                public ModuleInfo createFromParcel(Parcel source) {
                    return new ModuleInfo(source);
                }

                @Override
                public ModuleInfo[] newArray(int size) {
                    return new ModuleInfo[size];
                }
            };//we end an anonymous class with a semicolon since it is a class declaration but used as a statement

}
