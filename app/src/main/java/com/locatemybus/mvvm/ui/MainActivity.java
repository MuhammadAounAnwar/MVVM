package com.locatemybus.mvvm.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public static final int Add_Note_Request = 1;
    public static final int Edit_Note_Request = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, Add_Note_Request);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.deleteNote(adapter.getnoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnitemClickListener(new NoteAdapter.onItemClicklistener() {
            @Override
            public void itemClickListener(Note note) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra(EditNoteActivity.ExtraTitle, note.getTitle());
                intent.putExtra(EditNoteActivity.ExtraDescription, note.getDescription());
                intent.putExtra(EditNoteActivity.ExtraPriority, note.getPriority());
                intent.putExtra(EditNoteActivity.ExtraId, note.getId());

                startActivityForResult(intent, Edit_Note_Request);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Add_Note_Request && requestCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.ExtraTitle);
            String description = data.getStringExtra(AddNoteActivity.ExtraDescription);
            int priority = data.getIntExtra(AddNoteActivity.ExtraPriority, 0);


            Note note = new Note(title, description, priority);
            noteViewModel.insetNote(note);
            Toast.makeText(this, "note saved", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == Edit_Note_Request && requestCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.ExtraTitle);
            String description = data.getStringExtra(AddNoteActivity.ExtraDescription);
            int priority = data.getIntExtra(AddNoteActivity.ExtraPriority, 0);
            Note note = new Note(title, description, priority);
            int id = getIntent().getIntExtra(EditNoteActivity.ExtraId, -1);

            if (id == -1) {
                Toast.makeText(this, "unable to update.", Toast.LENGTH_SHORT).show();
            }else {
                note.setId(id);
            }
            noteViewModel.updateNote(note);

        } else {
            Toast.makeText(this, "note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.del_all_notes:
                noteViewModel.deleteAllNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
