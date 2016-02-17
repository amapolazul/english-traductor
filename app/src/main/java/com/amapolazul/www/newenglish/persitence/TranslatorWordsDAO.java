package com.amapolazul.www.newenglish.persitence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsmartinez on 17/03/2015.
 */
public class TranslatorWordsDAO {

    // Database fields
    private SQLiteDatabase database;
    private TranslatorSQLiteHelper dbHelper;
    private String[] allColumns = {
            TranslatorSQLiteHelper.COLUMN_NAME_ENTRY_ID,
            TranslatorSQLiteHelper.COLUMN_NAME_ESPANISH_TITLE,
            TranslatorSQLiteHelper.COLUMN_NAME_ENGLISH_TITLE,
            TranslatorSQLiteHelper.COLUMN_NAME_SPANISH_AUDIO_PATH,
            TranslatorSQLiteHelper.COLUMN_NAME_SPANISH_AUDIO_PATH};

    public TranslatorWordsDAO(Context context) {
        dbHelper = new TranslatorSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public TranslatorWordsInfo createWordDefinition(TranslatorWordsInfo wordsInfo) {
        ContentValues values = new ContentValues();
        values.put(TranslatorSQLiteHelper.COLUMN_NAME_ESPANISH_TITLE, wordsInfo.getEspanol_def());
        values.put(TranslatorSQLiteHelper.COLUMN_NAME_ENGLISH_TITLE, wordsInfo.getEnglish_def());
        values.put(TranslatorSQLiteHelper.COLUMN_NAME_SPANISH_AUDIO_PATH, wordsInfo.getEspanol_audio_path());
        values.put(TranslatorSQLiteHelper.COLUMN_NAME_ENGLISH_AUDIO_PATH, wordsInfo.getEnglish_audio_path());
        long insertId = database.insert(TranslatorSQLiteHelper.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(TranslatorSQLiteHelper.TABLE_NAME,
                allColumns, TranslatorSQLiteHelper.COLUMN_NAME_ENTRY_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TranslatorWordsInfo newTranslatorWordsInfo = cursorToComment(cursor);
        cursor.close();
        return newTranslatorWordsInfo;
    }

    /**
     * Devuelve la palabra a buscar haciendo concidir la definicion en español o en ingles
     * @param wordInfo
     * @return
     */
    public List<TranslatorWordsInfo> getWordEnglishSpanishInfo(String wordInfo) {
//        String selectQuery = "SELECT * FROM "+TranslatorSQLiteHelper.TABLE_NAME+" WHERE "+TranslatorSQLiteHelper.COLUMN_NAME_ENGLISH_TITLE+"=?";
          String selectQuery = "SELECT * FROM "+TranslatorSQLiteHelper.TABLE_NAME+" WHERE "+
                  "replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( lower("+TranslatorSQLiteHelper.COLUMN_NAME_ENGLISH_TITLE+"), 'á','a'), 'ã','a'), 'â','a'), 'é','e'), 'ê','e'), 'í','i'),'ó','o') ,'õ','o') ,'ô','o'),'ú','u'), 'ç','c') " +
                  "=?";
        Cursor c = database.rawQuery(selectQuery, new String[]{wordInfo.toLowerCase()});
        List<TranslatorWordsInfo> transWordInfoList = new ArrayList<TranslatorWordsInfo>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                TranslatorWordsInfo comment = cursorToComment(c);
                transWordInfoList.add(comment);
                c.moveToNext();
            }
            return transWordInfoList;
        } else {
            return null;
        }
    }

    public List<TranslatorWordsInfo> getWordSpanishEnglishInfo(String wordInfo) {
//        String selectQuery = "SELECT * FROM "+TranslatorSQLiteHelper.TABLE_NAME+" WHERE "+TranslatorSQLiteHelper.COLUMN_NAME_ESPANISH_TITLE+"=?";
        String selectQuery = "SELECT * FROM "+TranslatorSQLiteHelper.TABLE_NAME+" WHERE "+
                "replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( lower("+TranslatorSQLiteHelper.COLUMN_NAME_ESPANISH_TITLE+"), 'á','a'), 'ã','a'), 'â','a'), 'é','e'), 'ê','e'), 'í','i'),'ó','o') ,'õ','o') ,'ô','o'),'ú','u'), 'ç','c') " +
                "=?";
        Cursor c = database.rawQuery(selectQuery, new String[]{wordInfo.toLowerCase()});
        List<TranslatorWordsInfo> transWordInfoList = new ArrayList<TranslatorWordsInfo>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                TranslatorWordsInfo comment = cursorToComment(c);
                transWordInfoList.add(comment);
                c.moveToNext();
            }
            return transWordInfoList;
        } else {
            return null;
        }
    }

    public void removeAll()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TranslatorSQLiteHelper.TABLE_NAME, null, null);
    }

    public List<TranslatorWordsInfo> getAllWords() {
        List<TranslatorWordsInfo> transWordInfoList = new ArrayList<TranslatorWordsInfo>();

        Cursor cursor = database.query(TranslatorSQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TranslatorWordsInfo comment = cursorToComment(cursor);
            transWordInfoList.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return transWordInfoList;
    }

    private TranslatorWordsInfo cursorToComment(Cursor cursor) {
        TranslatorWordsInfo translatorWordsInfo = new TranslatorWordsInfo();
        translatorWordsInfo.set_id(cursor.getLong(0));
        translatorWordsInfo.setEspanol_def(cursor.getString(1));
        translatorWordsInfo.setEnglish_def(cursor.getString(2));
        translatorWordsInfo.setEspanol_audio_path(cursor.getString(3));
        translatorWordsInfo.setEnglish_audio_path(cursor.getString(4));
        return translatorWordsInfo;
    }
}