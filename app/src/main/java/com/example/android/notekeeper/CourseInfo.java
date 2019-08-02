package com.example.android.notekeeper;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
/**
 * This is about the information on the courses, CourseInfo
 * CourseInfo is a reference in NoteInfo which is a parcelable hence CourseInfo should be a Parcelable too
 * We are implementing Parcelable methods and interface(describeContents() and writeToParcel() )
 */

public final class CourseInfo implements Parcelable {
    private final String mCourseId;
    private final String mTitle;
    private final List<ModuleInfo> mModules;//of type ModuleInfo thus we have to make ModuleInfo Parcelable too

    public CourseInfo(String courseId, String title, List<ModuleInfo> modules) {
        mCourseId = courseId;
        mTitle = title;
        mModules = modules;
    }

    /*
    The constructor we are using to recreate our object from the parcel
    It has a parameter name source of type Parcel
    We use a private constructor since we might have fields marked as final(private and final go hand in hand)
    We should read the member in the same order as we wrote them
    Reading a Parcelable, we must pass in the class loader information of that type
    */
    private CourseInfo(Parcel source) {
        mCourseId = source.readString();
        mTitle = source.readString();
        mModules = new ArrayList<>();
        source.readTypedList(mModules, ModuleInfo.CREATOR);
    }

    public String getCourseId() {
        return mCourseId;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<ModuleInfo> getModules() {
        return mModules;
    }

    public boolean[] getModulesCompletionStatus() {
        boolean[] status = new boolean[mModules.size()];

        for(int i=0; i < mModules.size(); i++)
            status[i] = mModules.get(i).isComplete();

        return status;
    }

    public void setModulesCompletionStatus(boolean[] status) {
        for(int i=0; i < mModules.size(); i++)
            mModules.get(i).setComplete(status[i]);
    }

    public ModuleInfo getModule(String moduleId) {
        for(ModuleInfo moduleInfo: mModules) {
            if(moduleId.equals(moduleInfo.getModuleId()))
                return moduleInfo;
        }
        return null;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseInfo that = (CourseInfo) o;

        return mCourseId.equals(that.mCourseId);

    }

    @Override
    public int hashCode() {
        return mCourseId.hashCode();
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
        dest.writeString(mCourseId);
        dest.writeString(mTitle);
        dest.writeTypedList(mModules);
    }

    /*
    The static field CREATOR is used to recreate the object from the Parcel
    It must be of type Parcelable.Creator
    The info from this field will then be used to create new instances of our type hence the Parcelable.Creator interface is generic
    We then have to provide our type name as a generic argument.<CourseInfo>
    CREATOR is now able to create instances of type CourseInfo
    We then need to implement a class that can provide the Parcelable.Creator behaviour using an anonymous class(new part)
    */
    public static final Parcelable.Creator<CourseInfo> CREATOR =
            /*
            The anonymous class has two methods; createFromParcel and newArray of type CreateInfo
            createFromParcel creates a new type instance.
            Rather than setting all the values directly under createFromParcel, we'll use a private constructor CourseInfo with a variable source.
            When setting the values, they should be in the same order as in writeToParcel
            newArray creates an array CourseInfo with a variable size as its size
            */
            new Creator<CourseInfo>() {

                @Override
                public CourseInfo createFromParcel(Parcel source) {
                    return new CourseInfo(source);
                }

                @Override
                public CourseInfo[] newArray(int size) {
                    return new CourseInfo[size];
                }
            };//we end an anonymous class with a semicolon since it is a class declaration but used as a statement

}
