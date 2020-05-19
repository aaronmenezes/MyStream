package com.kyser.demosuite.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DataManager {

    private static DataManager __instance;

    private  DataManager() {

    }
    public static  DataManager getInstance(){
        if(__instance == null)
            __instance = new DataManager();
        return __instance;
    }

    public void demo(NoteKeeperHelper np){
        SQLiteDatabase db = np.getReadableDatabase();
        String [] lbl = {"interactive_lid"	,"interactive_iso",  "label_lid",  "label_iso"	,   "label_name"};
        Cursor language_cursor =  db.query("language",	lbl,null,null,null,null,null);
    }
}
