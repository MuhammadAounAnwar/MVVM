package com.locatemybus.mvvm.viewModel;

import android.app.Application;
import android.app.ListActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.locatemybus.mvvm.repositiory.NoteRepository;
import com.locatemybus.mvvm.room.entity.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        noteRepository=new NoteRepository(application);
        allNotes=noteRepository.getAllNotes();
    }

    public void insetNote(Note note){
        noteRepository.insert(note);
    }

    public void updateNote(Note note){
        noteRepository.update(note);
    }

    public void deleteNote(Note note){
        noteRepository.delete(note);
    }

    public void deleteAllNote(){
        noteRepository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }


}
