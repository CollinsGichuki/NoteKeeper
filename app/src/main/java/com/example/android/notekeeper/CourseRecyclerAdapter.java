package com.example.android.notekeeper;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/*
Adapter class that populates the RecyclerAdapter
It extends the RecyclerView.Adapter class and
we are going to use the NoteRecyclerAdapter.ViewHolder to hold the info for individual views

*/
public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<CourseInfo> mCourses;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerAdapter(Context mContext, List<CourseInfo> mCourses) {
        this.mContext = mContext;
        //We use the LayoutInflater class to create views from the Layout resource
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mCourses = mCourses;
    }

    @NonNull
    //Creates the ViewHolder instances and the views themselves
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup holder, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_course_list,holder,false);
        return new ViewHolder(itemView);
    }

    //Associates the data with our views
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseInfo course = mCourses.get(position);
        holder.mTextCourse.setText(course.getTitle());
        //Setting the position of current ViewHolder
        holder.mCurrentPosition = position;
    }

    //Indicates the number of data items we have
    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    //Holds the info that'll be displayed in the views
    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(),
                            Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
