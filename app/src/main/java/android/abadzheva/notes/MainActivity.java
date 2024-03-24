package android.abadzheva.notes;

import android.abadzheva.notes.adapter.NotesListAdapter;
import android.abadzheva.notes.database.RoomDataBase;
import android.abadzheva.notes.models.Note;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    FloatingActionButton fab;
    NotesListAdapter notesListAdapter;
    RoomDataBase roomDataBase;
    List<Note> notes = new ArrayList<>();
    SearchView searchViewHome;
    Note selectedNote;

    private static final String LOG_TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab = findViewById(R.id.add_note_fab_btn);
        searchViewHome = findViewById(R.id.searchViewHome);
        roomDataBase = RoomDataBase.getInstance(this);
        notes = roomDataBase.mainDAO().getAll();

        updateRecycler(notes);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
            startActivityForResult(intent, 101);
        });

        searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        Log.d(LOG_TAG, "Hello World");
        Log.d(LOG_TAG, "Hi, notes!!!");
    }

    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for (Note singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                    singleNote.getNote().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
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
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Note old_note = (Note) data.getSerializableExtra("note");
                roomDataBase.mainDAO().update(old_note.getID(), old_note.getTitle(), old_note.getNote());
                notes.clear();
                notes.addAll(roomDataBase.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Note> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter = new NotesListAdapter(MainActivity.this, notes, noteClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NoteClickListener noteClickListener = new NoteClickListener() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this, AddNewNoteActivity.class);
            intent.putExtra("old_note", note);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Note note, CardView cardView) {
            selectedNote = new Note();
            selectedNote = note;
            showPopUp(cardView);
        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                if (selectedNote.isPinned()) {
                    roomDataBase.mainDAO().pin(selectedNote.getID(), false);
                    Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
                } else {
                    roomDataBase.mainDAO().pin(selectedNote.getID(), true);
                    Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(roomDataBase.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                roomDataBase.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Note removed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}