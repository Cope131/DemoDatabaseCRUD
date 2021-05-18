package com.myapplicationdev.android.demodatabasecrud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Database Helper
    DBHelper dbHelper = new DBHelper(this);

    // Data Notes
    ArrayList<Note> notes = new ArrayList<>();

    // Views
    private Button insertButton, editButton, retrieveButton;
    private TextInputEditText noteTextInputEditText;
    private TextView dbContentTextView;
    private ListView dbContentListView;

    //
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        dbContentListView.setAdapter(arrayAdapter);
    }

    private void initViews() {
        insertButton = findViewById(R.id.insert_button);
        editButton = findViewById(R.id.edit_button);
        retrieveButton = findViewById(R.id.retrieve_button);
        insertButton.setOnClickListener(this::onClick);
        editButton.setOnClickListener(this::onClick);
        retrieveButton.setOnClickListener(this::onClick);

        noteTextInputEditText = findViewById(R.id.note_text_input_edit_text);

        dbContentTextView = findViewById(R.id.db_content_text_view);

        dbContentListView = findViewById(R.id.db_content_list_view);
    }

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
                doRetrieve();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG + "in onActivityResult", resultCode + "," +  requestCode);
        if (requestCode == 9 && resultCode == RESULT_OK) {
            // Refresh Text View to Display content of database
            retrieveButton.performClick();
            arrayAdapter.notifyDataSetChanged();
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

        doRetrieve();
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

    private void doRetrieve() {
        notes.clear();
        notes = dbHelper.getAllNotes();
        Log.d(TAG + "doRetrieve", "Size: " + notes.size());
        StringBuilder stringBuilder = new StringBuilder();
        for (Note note: notes) {
            stringBuilder.append(String.format("ID: %d, %s\n", note.getId(), note.getNoteContent()));
        }
        dbContentTextView.setText(stringBuilder.toString());
        arrayAdapter.notifyDataSetChanged();
        dbHelper.close();
    }
}