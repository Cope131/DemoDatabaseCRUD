package com.myapplicationdev.android.demodatabasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_button:
                String noteText = noteTextInputEditText.getText() + "";
                if (!noteText.trim().isEmpty()) {
                    long result = dbHelper.insertNote(noteText);
                    if (result != -1) {
                        Toast.makeText(this, "Successfully inserted note", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to insert note", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter note", Toast.LENGTH_SHORT).show();
                }
                dbHelper.close();
                break;
            case R.id.edit_button:
                if (!notes.isEmpty()) {
                    // Navigate to Edit Activity
                    Intent intent = new Intent(this, EditActivity.class);
                    intent.putExtra("note", notes.get(0));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Retrieve notes first", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.retrieve_button:
                notes.clear();
                notes = dbHelper.getAllNotes();
                StringBuilder stringBuilder = new StringBuilder();
                for (Note note: notes) {
                    stringBuilder.append(String.format("ID: %d, %s\n", note.getId(), note.getNoteContent()));
                }
                dbContentTextView.setText(stringBuilder.toString());
                dbHelper.close();
                break;
        }
    }
}