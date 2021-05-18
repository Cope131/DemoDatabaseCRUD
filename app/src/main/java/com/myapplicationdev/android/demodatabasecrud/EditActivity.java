package com.myapplicationdev.android.demodatabasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditActivity.class.getSimpleName();

    // Database Helper
    private DBHelper dbHelper = new DBHelper(this);

    // Views
    private TextView noteIdTextView;
    private TextInputEditText noteContentTextInputEditText;
    private Button updateButton, deleteButton;

    // Selected Note
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initViews();

        // Get Note Object
        selectedNote = (Note) getIntent().getSerializableExtra("data");

        // Set Text of Views
        noteIdTextView.setText("" + selectedNote.getId());
        noteContentTextInputEditText.setText(selectedNote.getNoteContent());

        // Update Title of Action Bar
        getSupportActionBar().setTitle("Edit Note");

    }

    private void initViews() {
        noteIdTextView = findViewById(R.id.id_text_view);
        noteContentTextInputEditText = findViewById(R.id.edit_note_text_input_edit_text);
        updateButton = findViewById(R.id.update_button);
        deleteButton = findViewById(R.id.delete_button);
        updateButton.setOnClickListener(this::onClick);
        deleteButton.setOnClickListener(this::onClick);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.update_button:
                doUpdate();
                 break;
            case R.id.delete_button:
                doDelete();
                break;
        }

        if (id == updateButton.getId() || id == deleteButton.getId()) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void doUpdate() {
        String editNoteText = noteContentTextInputEditText.getText() + "";
        if (!editNoteText.trim().isEmpty()) {
            selectedNote.setNoteContent(editNoteText);
            int result = dbHelper.updateNote(selectedNote);
            Log.d(TAG, "update result" + result);
            if (result == 1) {
                Toast.makeText(this, "Successfully updated note", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter note", Toast.LENGTH_SHORT).show();
        }

    }

    private void doDelete() {
        int result = dbHelper.deleteNote(selectedNote.getId());
        Log.d(TAG + "in doDelete", "Result > " + result);
        if (result == 1) {
            Toast.makeText(this, "Successfully deleted note", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
        }

    }

}