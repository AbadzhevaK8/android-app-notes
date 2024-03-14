package android.abadzheva.notes;

import android.abadzheva.notes.adapter.NotesListAdapter;
import android.abadzheva.notes.database.RoomDataBase;
import android.abadzheva.notes.models.Note;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    NotesListAdapter notesListAdapter;
    RoomDataBase roomDataBase;
    List<Note> notes = new ArrayList<>();

    private static final String LOG_TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab = findViewById(R.id.add_note_fab_btn);

        roomDataBase = RoomDataBase.getInstance(this);
        notes = roomDataBase.mainDAO().getAll();

        updateRecycler(notes);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
            startActivityForResult(intent,101);
        });

        Log.d(LOG_TAG, "Hello World");
        Log.d(LOG_TAG, "Hi, notes!!!");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            assert data != null;
            Note new_note = (Note) data.getSerializableExtra("note");
            roomDataBase.mainDAO().insert(new_note);
            notes.clear();
            notes.addAll(roomDataBase.mainDAO().getAll());
            notesListAdapter.notifyDataSetChanged();
        }
    }

    private void updateRecycler(List<Note> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, noteClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NoteClickListener  noteClickListener = new NoteClickListener() {
        @Override
        public void onClick(Note note) {

        }

        @Override
        public void onLongClick(Note note, CardView cardView) {

        }
    };
}