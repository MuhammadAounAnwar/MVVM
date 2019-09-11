package com.locatemybus.mvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.locatemybus.mvvm.R;

public class EditNoteActivity extends AppCompatActivity {

    public static final String ExtraId =
            "com.locatemybus.mvvm.ui_ExtraId";
    public static final String ExtraTitle =
            "com.locatemybus.mvvm.ui_ExtraTitle";
    public static final String ExtraDescription =
            "com.locatemybus.mvvm.ui_ExtraDescription";
    public static final String ExtraPriority =
            "com.locatemybus.mvvm.ui_ExtraPriority";

    private EditText editText_title, editText_description;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editText_title = findViewById(R.id.editText);
        editText_description = findViewById(R.id.editText2);
        numberPicker = findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(ExtraId)) {
            setTitle("Edit Note");
            editText_title.setText(intent.getStringExtra(ExtraTitle));
            editText_description.setText(intent.getStringExtra(ExtraDescription));
            numberPicker.setValue(intent.getIntExtra(ExtraPriority, 0));
        } else {
            setTitle("Add Note");
        }


    }

    private void saveNote() {
        String title = editText_title.getText().toString();
        String description = editText_description.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please Insert a Title.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(ExtraTitle, title);
        data.putExtra(ExtraDescription, description);
        data.putExtra(ExtraPriority, priority);

        int id = getIntent().getIntExtra(ExtraId, -1);

        if (id != -1) {
            data.putExtra(ExtraId, id);
        }

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.saveItem) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
