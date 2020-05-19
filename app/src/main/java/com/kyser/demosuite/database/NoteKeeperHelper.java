package com.kyser.demosuite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NoteKeeperHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "media.db";
    private static final int VERSION =1 ;

    public NoteKeeperHelper(Context context) {
        super(context, DATABASENAME,null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
