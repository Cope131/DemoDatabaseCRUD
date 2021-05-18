package com.myapplicationdev.android.demodatabasecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    // Database
    private static final String DATABASE_NAME = "simplenotes.db";
    private static final int DATABASE_VERSION = 2;
    // Table
    private static final String TABLE_NOTE = "note";
    // Columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_CONTENT = "note_content";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = String.format("CREATE TABLE %s(", TABLE_NOTE);
        createTableSql += String.format("%s INTEGER PRIMARY KEY AUTOINCREMENT, ", COLUMN_ID);
        createTableSql += String.format("%s TEXT", COLUMN_NOTE_CONTENT);
        createTableSql += ")";
        // create table
        db.execSQL(createTableSql);
        Log.d(TAG + " in onCreate",  "\n" + createTableSql);
        Log.d(TAG + " in onCreate", "Created Note Table.");
        // sample notes
        for (int i = 0; i < 4; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NOTE_CONTENT, "Data number " + i);
            db.insert(TABLE_NOTE, null, contentValues);
        }
        Log.i(TAG + "in onCreate", "sample records inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        // onCreate(db);
        // db.execSQL();
    }

    public long insertNote(String noteContent) {
        // values
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTE_CONTENT, noteContent);
        // insert values
        SQLiteDatabase writableDatabase = getWritableDatabase();
        long result = writableDatabase.insert(TABLE_NOTE, null, contentValues);
        // Result returns either the ID or -1 (Failed to insert)
        Log.d(TAG + " in insertNote",  result + "");
        writableDatabase.close();
        return result;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        // SQL Statement - Select Query
        String selectQuery = String.format(
                "SELECT %s, %s FROM %s",
                COLUMN_ID, COLUMN_NOTE_CONTENT, TABLE_NOTE
        );
        // Cursor
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(selectQuery, null);
        // Retrieve Values of Each Row
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                // Add Note Object to ArrayList
                Note note = new Note(id, noteContent);
                notes.add(note);
            }
        }

        // Close
        cursor.close();
        readableDatabase.close();

        return notes;
    }

    public int updateNote(Note note) {
        // new values
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTE_CONTENT, note.getNoteContent());
        // filter
        String condition = String.format("%s = ?", COLUMN_ID);
        String[] args = {note.getId() + ""};
        // update - returns the affected rows
        //        - < 1 no rows affected (updated)
        //        - update all when args is null
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int result = writableDatabase.update(TABLE_NOTE, contentValues, condition, args);
        // close
        writableDatabase.close();
        Log.d(TAG + " in updateNote",  result + "");

        return result;
    }

    public int deleteNote(int id){
        // filter
        String condition = String.format("%s = ?", COLUMN_ID);
        String[] args = {id + ""};
        // delete
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        int result = writableDatabase.delete(TABLE_NOTE, condition, args);
        // close
        writableDatabase.close();

        return result;
    }

    public ArrayList<Note> getAllNotes(String keyword) {
        ArrayList<Note> notes = new ArrayList<>();

        // filter
        String[] columns = {COLUMN_ID, COLUMN_NOTE_CONTENT};
        String condition = String.format("%s LIKE ?", COLUMN_NOTE_CONTENT);
        String[] args = {String.format("%% %s %%", keyword)};

        // get notes
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor cursor = readableDatabase.query(
                TABLE_NOTE,
                columns,
                condition, args,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
            } while (cursor.moveToNext());
        }

        // close
        cursor.close();
        readableDatabase.close();

        return notes;

    }





}
