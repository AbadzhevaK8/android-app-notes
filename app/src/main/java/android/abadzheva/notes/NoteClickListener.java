package android.abadzheva.notes;

import android.abadzheva.notes.models.Note;

import androidx.cardview.widget.CardView;

public interface NoteClickListener {

    void onClick(Note note);
    void onLongClick(Note note, CardView cardView);
}
