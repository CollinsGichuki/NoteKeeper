package com.example.android.notekeeper;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
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
public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<NoteInfo> mNotes;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerAdapter(Context mContext, List<NoteInfo> mNotes) {
        this.mContext = mContext;
        //We use the LayoutInflater class to create views from the Layout resource
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mNotes = mNotes;
    }

    @NonNull
    //Creates the ViewHolder instances and the views themselves
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup holder, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list,holder,false);
        return new ViewHolder(itemView);
    }

    //Associates the data with our views
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.mTextCourse.setText(note.getCourse().getTitle());
        holder.mTextTitle.setText(note.getTitle());
        //Setting the position of current ViewHolder
        holder.mCurrentPosition = position;
    }

    //Indicates the number of data items we have
    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    //Holds the info that'll be displayed in the views
    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;
        public final TextView mTextTitle;
        public int mCurrentPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
            mTextTitle = (TextView) itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION, mCurrentPosition);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
