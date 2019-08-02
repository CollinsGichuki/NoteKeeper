package com.example.android.notekeeper;

/*
This is the first activity when the app loads
* It holds all the notes in our app
* From here, we can also create a new note
*/

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter mNoteRecyclerAdapter;

    /*
    Since we are using a recyclerView, we need a different adapter
    private ArrayAdapter<NoteInfo> mAdapterNotes;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        * The FloatingBar(new) is used to create a new note
        */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
             }
        });

        initializeDisplayContent();
    }

    //Gets called when the NoteList activity appears in the foreground again.
    //Handles the interaction needed when a new note has been written
    @Override
    protected void onResume() {
        super.onResume();
        /*
        Tells the NoteList activity to be prepared in case there is a new note, but we are using a new type of an adapter
        mAdapterNotes.notifyDataSetChanged();
        */
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        //We are using a different kind of an adapter, this code is relevant atm
        //
        //ListView(content_note_list) that contains all the notes
        //ListNotes is an object of type ListView that references list_notes
        //It is marked as final so that we can then reference it inside the anonymous class AdapterView.OnItemClickListener
        //
        //final ListView listNotes = (ListView) findViewById(R.id.list_notes);

        /*
        Populating the NoteList Activity
        We use a DataManager to fetch the data
        We then equate it to a variable notes of type List with a generic argument NoteInfo
        We then use an ArrayAdapter to populate our ListView(ListNotes)
        */
        //List<NoteInfo> notes = DataManager.getInstance().getNotes();
        //mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        //listNotes.setAdapter(mAdapterNotes);

        /*
        To handle the interaction of when a user selects a note from the notes list,
        we add an event listener on the ListNotes variable(ListView)
        setOnItemClickListener usually accepts a reference to an interface.
        We then new the interface rather than the class due to Java's ability to create anonymous classes
        OnItemClickListener interface is in the AdapterView class
        */
        //listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //@Override
            /*
            onItemClick will be called whenever a user makes a selection on the ListView
            The intent then starts the NoteActivity class(Activities are a type of context thus we use this)
            Since the interface implementation is an implementation of an anonymous class
            and we want 'this' to reference the NoteListActivity thus we need to explicitly write it(NoteListActivity.this)

            */
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //The intent then starts the NoteActivity
                //Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                /*
                We get the position of the note a user clicked and a reference to the ListView(we can cast the parent param into a ListView)
                We use the local variable ListNotes and cast it to get the reference of the ListView since it's easier
                We then put it as an extra(name value pairs, which are constant Strings) and the note in the intent using Parcelable
                */
                /*
                Since both the NoteListActivity and the NoteActivity are in the same process,
                we don't need to directly fetch the note ourselves,
                We provide the NoteActivity with the position of the note and it can fetch it itself from the DataManager class
                thus we pass note as the variable position as the param rather than note
                ** NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);**
                */
                //intent.putExtra(NoteActivity.NOTE_POSITION, position);
                //startActivity(intent);
           // }
       //});// End of an anonymous class

        //Reference to our recyclerView
        final RecyclerView recyclerNotes = (RecyclerView) findViewById(R.id.list_notes);
        //Since RecyclerView needs a layout Manager
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerNotes.setLayoutManager(notesLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);
        recyclerNotes.setAdapter(mNoteRecyclerAdapter);

    }

}
