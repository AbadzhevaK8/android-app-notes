package android.abadzheva.notes.adapter;

import android.abadzheva.notes.NoteClickListener;
import android.abadzheva.notes.R;
import android.abadzheva.notes.models.Note;
import android.content.Context;
import android.content.res.Configuration;
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

        holder.noteItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.noteItemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.noteItemContainer);
                return true;
            }
        });
    }

    private int getRandomColor() {
        List<Integer> lightColorCode = new ArrayList<>();
        List<Integer> darkColorCode = new ArrayList<>();

        lightColorCode.add(R.color.light_note_color_1);
        lightColorCode.add(R.color.light_note_color_2);
        lightColorCode.add(R.color.light_note_color_3);
        lightColorCode.add(R.color.light_note_color_4);
        lightColorCode.add(R.color.light_note_color_5);
        lightColorCode.add(R.color.light_note_color_6);
        lightColorCode.add(R.color.light_note_color_7);

        darkColorCode.add(R.color.dark_note_color_1);
        darkColorCode.add(R.color.dark_note_color_2);
        darkColorCode.add(R.color.dark_note_color_3);
        darkColorCode.add(R.color.dark_note_color_4);
        darkColorCode.add(R.color.dark_note_color_5);
        darkColorCode.add(R.color.dark_note_color_6);
        darkColorCode.add(R.color.dark_note_color_7);

        int color;
        if (isDarkTheme(context)) {
            color = darkColorCode.get(new Random().nextInt(darkColorCode.size()));
        } else {
            color = lightColorCode.get(new Random().nextInt(lightColorCode.size()));
        }
        return color;
    }

    private boolean isDarkTheme(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Note> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
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