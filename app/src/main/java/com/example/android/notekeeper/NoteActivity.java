package com.example.android.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    /*
    !Fields in the class!
    String constant
    Qualified the constant in the package name since info from IntentExtra can come from multiple sources, we do this to make it unique
    We sue fields since we can access them from anywhere in the class
    */
    public static final String NOTE_POSITION = "com.example.android.notekeeper.NOTE_POSITION";
    /*
        We are destroying our activities as soon as we leave them hence we store the state of the original activity values in a bundle
        Creating fields for the Bundle
        */
    public static final String ORIGINAL_NOTE_COURSE_ID ="com.example.android.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "come.example.android.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.eexample.android.notekeeper.ORIGINAL_NOTE_TEXT";

    public static final int POSITION_NOT_SET = -1;//The default position of the intent extra containing the note position
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private Boolean mIsCancelling = false;
    private String mOriginalCourseNoteId;
    private String mOrigianlNoteTile;
    private String mOriginalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);

        //ListVIew for courses
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        /* The adapter fetches the data then displays the data */
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        //Gets the note from the intent extra values
        readDisplayStateValues();
        /*
        Since we are destroying activities and recreating them from scratch,
        we check if teh activity is being created for the first time
        If it's a null savedInstanceState, hence for the first time
        we save the original note from the intent extras
        If we are recreating the activity,
        we restore the state of the values of the previous activity from the bundle
        */
        if(savedInstanceState == null){
            saveOriginalNoteValues();
        }else{
            restoreOriginalNoteValue(savedInstanceState);
        }

        //References to the two EditTexts in the NoteActivity
        mTextNoteTitle = (EditText) findViewById(R.id.text_note_title);
        mTextNoteText = (EditText) findViewById(R.id.text_note_text);

        //If it is a new note, display an empty NoteActivity
        if(!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
    }

    private void restoreOriginalNoteValue(Bundle savedInstanceState) {
        mOriginalCourseNoteId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOrigianlNoteTile = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }

    //Saves the values(id, title and text) of the note after getting it from the intent extras
    private void saveOriginalNoteValues() {
        if(mIsNewNote){
            return;//If it's a new note return void
        }

        mOriginalCourseNoteId = mNote.getCourse().getCourseId();
        mOrigianlNoteTile = mNote.getTitle();
        mOriginalNoteText = mNote.getText();

    }
    //
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Put the values we want to save
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalCourseNoteId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOrigianlNoteTile);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        //We first get the list of courses from the DataManager
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        //Get the index of the course we want to populate
        int courseIndex = courses.indexOf(mNote.getCourse());
        //Setting the courses but since they're in a spinner, we have to select the one to display using its index
        spinnerCourses.setSelection(courseIndex);
        //Setting the textNoteTitle and textNoteText editTexts
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    /*
    Getting a reference to the intent that was used to start this activity  and the extra(constant NOTE_POSITION) that contain the note
    In the case for creating a new note, there will be no extras hence we use the boolean mIsNewNote
     */
    private void readDisplayStateValues() {
        Intent intent = getIntent();
        /*
        Since we are getting the position of the note, we use getIntExtra.
        POSITION_NOT_SET contains the value if the intent get no value(POSITION) back
        */
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        //Checks if it's a new note
        mIsNewNote = position == POSITION_NOT_SET;
        //If it's not a new note, get the DataManager instance, get the notes from it and get the position of the note
        if(mIsNewNote){
            createNewNote();
        }else {
            //If it's not a new note, get the DataManager instance, get the notes from it and get the position of the note
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        //Getting the reference to the DataManager
        DataManager dm = DataManager.getInstance();
        //Getting the position of the newly created note
        mNotePosition = dm.createNewNote();
        //Getting the new note at the position we've received and assigning it to mNote
        mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //When the user clicks the menu item(top right options)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();

        //Handling the menu item selection
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }else if(id == R.id.action_cancel){
            mIsCancelling = true;
            //Exit from the activity
            finish();
            //Then handle onPause
        }else if(id == R.id.action_next){
            moveNext();
        }else if(id == R.id.action_back){
            moveBack();
        }

        return super.onOptionsItemSelected(item);
    }

    private void moveBack() {
        //Save the changes to the current note
        saveNote();
        //Decrementing the Note Position number(previous note) and then displaying it
        --mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);

        displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);

        //invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        MenuItem item_2 = menu.findItem(R.id.action_back);
        //The next menu item should be enabled as long as we are not at the last note.
        int lastNoteIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNotePosition < lastNoteIndex);

        /*
        The back menu item should be enabled as long as we are not at the first note.

         int firstNoteIndex = DataManager.getInstance().getNotes().size();
        int one = 0;
        if(firstNoteIndex == 0){
            firstNoteIndex = one;
        }
        item_2.setEnabled(mNotePosition > firstNoteIndex);

        */

        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {


        //Save the changes to the current note
        saveNote();
        //Incrementing the Note Position number(next note) and then displaying it
        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
        //In the case a user cancels the changes the made to a note, we save the original state of it beforehand
        saveOriginalNoteValues();
        displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
        invalidateOptionsMenu();
    }

    /*
    onPause is called when the activity is no longer in the foreground
    Saving the note automatically when the user leaves the note
    */
    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling) {
            //Removing the new note from the backing store
            if(mIsNewNote){
                DataManager.getInstance().removeNote(mNotePosition);
            } else{
                //When the user hits cancel on a note, we explicitly store the previous state of that note.
                storePreviousNoteValues();
            }
        }else {
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        //Getting the state of the original note
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalCourseNoteId);

        mNote.setCourse(course);
        mNote.setTitle(mOrigianlNoteTile);
        mNote.setText(mOriginalNoteText);
    }

    private void saveNote() {
        /*
        Saving the value of whatever course that's selected in the spinner
        Saving the value of the Title and text that's on the title and text edit boxes
        */
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void sendEmail() {
        //Getting the course selected and storing it in the variable course
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        //Get the Title of the note which is in editable thus need for toString
        String subject = mTextNoteTitle.getText().toString();
        //The body of the mail will contain some hard coded text\Title of the course\the note.
        String text = "Check out what i learned in the PluralSight course \"" +
                course.getTitle() + "\"\n" + mTextNoteText.getText().toString();
        //The intent to send the mail
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        //Adding the mail extras(subject and body)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);



    }
}
