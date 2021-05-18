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
    DBHelper dbHelper = new DBHelper(this);

    // Views
    private TextView noteIdTextView;
    private TextInputEditText noteContentTextInputEditText;
    private Button updateButton, deleteButton;

    private Note data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initViews();

        // Get Note Object
        data = (Note) getIntent().getSerializableExtra("note");

        // Set Text of Views
        noteIdTextView.setText("ID:\n" + data.getId());
        noteContentTextInputEditText.setText(data.getNoteContent());
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
        switch (v.getId()) {
            case R.id.update_button:
                String editNoteText = noteContentTextInputEditText.getText() + "";
                if (!editNoteText.trim().isEmpty()) {
                    data.setNoteContent(editNoteText);
                    int result = dbHelper.updateNote(data);
                    Log.d(TAG, "update result" + result);
                    if (result == 1) {
                        Toast.makeText(this, "Successfully updated note", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter note", Toast.LENGTH_SHORT).show();
                }
                 break;
            case R.id.delete_button:
                break;
        }
    }
}