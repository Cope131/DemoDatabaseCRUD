package com.myapplicationdev.android.demodatabasecrud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Database Helper
    private DBHelper dbHelper = new DBHelper(this);

    // Views
    private Button insertButton, editButton, retrieveButton;
    private TextInputEditText noteTextInputEditText;
    private TextView dbContentTextView;
    private ListView dbContentListView;
    private CheckBox displayListCheckbox, displayTextCheckbox;

    // Track Show Content Options
    private boolean isTextContentShown, isListContentShown;


    // Data - Notes
    private ArrayList<Note> notes;

    // List View Components
    private ArrayAdapter<Note> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        initViews();

        // Initialize List View Components
        notes = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        dbContentListView.setAdapter(arrayAdapter);

        // Update State of Content Options (to track)
        isListContentShown = displayListCheckbox.isChecked();
        isTextContentShown = displayTextCheckbox.isChecked();

        // Show All Notes
        doRetrieve(isTextContentShown, isListContentShown);

    }


    private void initViews() {
        // Buttons
        insertButton = findViewById(R.id.insert_button);
        editButton = findViewById(R.id.edit_button);
        retrieveButton = findViewById(R.id.retrieve_button);
        insertButton.setOnClickListener(this::onClick);
        editButton.setOnClickListener(this::onClick);
        retrieveButton.setOnClickListener(this::onClick);
        // Input
        noteTextInputEditText = findViewById(R.id.note_text_input_edit_text);
        // Display Content
        dbContentTextView = findViewById(R.id.db_content_text_view);
        dbContentListView = findViewById(R.id.db_content_list_view);
        dbContentListView.setOnItemClickListener(this::onItemClick);
        // Display Options
        displayTextCheckbox = findViewById(R.id.display_text_content_check_box);
        displayListCheckbox = findViewById(R.id.display_list_content_check_box);
        displayTextCheckbox.setOnCheckedChangeListener(this::onCheckedChanged);
        displayListCheckbox.setOnCheckedChangeListener(this::onCheckedChanged);
    }

    // View is Clicked - Buttons
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_button:
                doInsert();
                break;
            case R.id.edit_button:
                doEdit();
                break;
            case R.id.retrieve_button:
                doRetrieve(isTextContentShown, isListContentShown);
                break;
        }
    }

    // Item is Clicked in List View
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note selectedNote = notes.get(position);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("data", selectedNote);
        startActivityForResult(intent, 9);
    }

    // Check or Uncheck options
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.display_text_content_check_box:
                isTextContentShown = isChecked;
                break;
            case R.id.display_list_content_check_box:
                isListContentShown = isChecked;
        }
        doRetrieve(isTextContentShown, isListContentShown);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG + "in onActivityResult", resultCode + "," +  requestCode);
        if (requestCode == 9 && resultCode == RESULT_OK) {
            // Load Text View & List View to Display Notes
            retrieveButton.performClick();
        }
    }

    private void doInsert() {
        String noteText = noteTextInputEditText.getText() + "";
        if (!noteText.trim().isEmpty()) {
            long result = dbHelper.insertNote(noteText);
            if (result != -1) {
                Toast.makeText(this, "Successfully inserted note", Toast.LENGTH_SHORT).show();
                noteTextInputEditText.getText().clear();
            } else {
                Toast.makeText(this, "Failed to insert note", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter note", Toast.LENGTH_SHORT).show();
        }
        dbHelper.close();

        doRetrieve(isTextContentShown, isListContentShown);
    }

    private void doEdit() {
        if (!notes.isEmpty()) {
            // Navigate to Edit Activity
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("data", notes.get(0));
            // startActivity(intent);
            startActivityForResult(intent, 9);
        } else {
            Toast.makeText(this, "Retrieve notes first", Toast.LENGTH_SHORT).show();
        }
    }

    private void doRetrieve(boolean showText, boolean showList) {
        // Retrieve All Notes from Database
        notes.clear();
        notes.addAll(dbHelper.getAllNotes());
        dbHelper.close();

        // Display using List View
        arrayAdapter.notifyDataSetChanged();

        // Display using Text View
        StringBuilder stringBuilder = new StringBuilder();
        for (Note note: notes) {
            stringBuilder.append(note.toString());
        }
        dbContentTextView.setText(stringBuilder.toString());

        // Show Text or/and List
        if (showList) {
            dbContentListView.setVisibility(View.VISIBLE);
        } else {
            dbContentListView.setVisibility(View.GONE);
        }

        if (showText) {
            dbContentTextView.setVisibility(View.VISIBLE);
        } else {
            dbContentTextView.setVisibility(View.GONE);
        }

    }

}