package com.example.coruseratingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class CourseAdapter extends FirestoreRecyclerAdapter<Courses, CourseAdapter.CourseHolder> {
    private onItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CourseAdapter(@NonNull FirestoreRecyclerOptions<Courses> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CourseHolder holder, int position, @NonNull Courses model) {
        holder.textViewTitle.setText(model.getCourseName());
        holder.textViewDescription.setText(model.getCourseDesc());
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.litem_list, viewGroup,false);
        return new CourseHolder(v);
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;



        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //Makes sure you cant click element out of bounds and null object
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });

        }
    }
    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;

    }
}
