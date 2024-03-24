package android.abadzheva.notes;

import android.abadzheva.notes.models.Note;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextNote;
    ImageView imageViewSave;
    Note note;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewSave = findViewById(R.id.image_view_save);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextNote = findViewById(R.id.edit_text_note);

        note = new Note();
        try {
            note = (Note) getIntent().getSerializableExtra("old_note");
            editTextTitle.setText(note.getTitle());
            editTextNote.setText(note.getNote());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        imageViewSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String desc = editTextNote.getText().toString();

            if (desc.isEmpty()) {
                Toast.makeText(AddNewNoteActivity.this, "Please, add a note.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (title.isEmpty()) {
                title = "No title";
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm", Locale.getDefault());
            Date date = new Date();

            if (!isOldNote) {
                note = new Note();
            }

            note.setTitle(title);
            note.setNote(desc);
            note.setDate(formatter.format(date));

            Intent intent = new Intent();
            intent.putExtra("note", note);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}