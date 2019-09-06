package com.locatemybus.mvvm.room.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.locatemybus.mvvm.room.dao.NoteDao;
import com.locatemybus.mvvm.room.entity.Note;

@Database(entities = Note.class ,version = 1)
public abstract class NoteDataBase extends RoomDatabase {

    private static NoteDataBase instance;
    public abstract NoteDao noteDao();
    public static synchronized NoteDataBase getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDataBase.class,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDataBase noteDataBase) {
            this.noteDao = noteDataBase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insertNote(new Note("Note 1","First 1",1));
            noteDao.insertNote(new Note("Note 2","First 2",2));
            noteDao.insertNote(new Note("Note 3","First 3",3));
            return null;
        }
    }

}
