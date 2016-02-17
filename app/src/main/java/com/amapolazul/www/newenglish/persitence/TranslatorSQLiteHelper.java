package com.amapolazul.www.newenglish.persitence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jsmartinez on 17/03/2015.
 */
public class TranslatorSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "translator_words_info";
    public static final String COLUMN_NAME_ENTRY_ID = "_id";
    public static final String COLUMN_NAME_ESPANISH_TITLE = "espanol_def";
    public static final String COLUMN_NAME_ENGLISH_TITLE = "english_def";
    public static final String COLUMN_NAME_ENGLISH_AUDIO_PATH = "english_audio_path";
    public static final String COLUMN_NAME_SPANISH_AUDIO_PATH = "espanol_audio_path";

    private static final String DATABASE_NAME = "translator.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_NAME_ENTRY_ID
            + " integer primary key autoincrement, " + COLUMN_NAME_ESPANISH_TITLE
            + " text not null," + COLUMN_NAME_ENGLISH_TITLE
            + " text not null," + COLUMN_NAME_SPANISH_AUDIO_PATH
            + " text not null," + COLUMN_NAME_ENGLISH_AUDIO_PATH
            + " text not null" +
            ");";

    public TranslatorSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}