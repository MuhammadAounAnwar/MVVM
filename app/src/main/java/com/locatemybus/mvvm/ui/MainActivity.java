package com.locatemybus.mvvm.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.locatemybus.mvvm.Adapter.NoteAdapter;
import com.locatemybus.mvvm.R;
import com.locatemybus.mvvm.repositiory.NoteRepository;
import com.locatemybus.mvvm.room.entity.Note;
import com.locatemybus.mvvm.viewModel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int Add_Note_Request=1;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,Add_Note_Request);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel= ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Add_Note_Request && requestCode==RESULT_OK){
            String title=data.getStringExtra(AddNoteActivity.ExtraTitle);
            String description=data.getStringExtra(AddNoteActivity.ExtraDescription);
            int priority=data.getIntExtra(AddNoteActivity.ExtraPriority,0);


            Note note=new Note(title,description,priority);
            noteViewModel.insetNote(note);
            Toast.makeText(this, "note saved", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
