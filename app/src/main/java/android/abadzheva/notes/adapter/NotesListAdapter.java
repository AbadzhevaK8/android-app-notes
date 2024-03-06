package android.abadzheva.notes.adapter;

import android.abadzheva.notes.NoteClickListener;
import android.abadzheva.notes.R;
import android.abadzheva.notes.models.Note;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Note> list;

    NoteClickListener listener;

    public NotesListAdapter(Context context, List<Note> list, NoteClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textTitle.setText(list.get(position).getTitle());
        holder.textTitle.setSelected(true);

        holder.textNote.setText(list.get(position).getNote());

        holder.textDate.setText(list.get(position).getDate());
        holder.textDate.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.imageViewPin.setImageResource(R.drawable.ic_pin_24);
        } else {
            holder.imageViewPin.setImageResource(0);
        }
        int colorForCard = getRandomColor();
        holder.noteItemContainer.setCardBackgroundColor(holder.itemView.getResources().getColor(colorForCard, null));
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.color6);
        colorCode.add(R.color.color7);
        Random random = new Random();
        return random.nextInt(colorCode.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView noteItemContainer;
    TextView textTitle, textNote, textDate;
    ImageView imageViewPin;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        noteItemContainer = itemView.findViewById(R.id.note_item_card);
        textTitle = itemView.findViewById(R.id.textView_title);
        textNote = itemView.findViewById(R.id.text_view_note);
        textDate = itemView.findViewById(R.id.text_view_date);
        imageViewPin = itemView.findViewById(R.id.imageView_pin);
    }
}