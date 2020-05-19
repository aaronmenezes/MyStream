package com.kyser.demosuite.database;

public class DatabaseContract {

    private DatabaseContract() {}

    public static final class NoteList{
        public static final String tableName = "note_list";
        public static final String noteId = "note_id";
        public static final String noteName = "note_name";
        public static final String SQL_Table = "CREATE TABLE " +tableName +" ( "+noteId+" Text , "+noteName+" Text ) ";
        
    }
}
