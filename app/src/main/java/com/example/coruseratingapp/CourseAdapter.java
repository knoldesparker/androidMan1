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

    //Sets the fields in the recycler view based on the Courses (model)
    @Override
    protected void onBindViewHolder(@NonNull CourseHolder holder, int position, @NonNull Courses model) {
        holder.textViewTitle.setText(model.getCourseName());
        holder.textViewDescription.setText(model.getCourseDesc());
    }


    //Inflates the layout based on the xml file, item_list, and reruns a CourseHOlder, to hold the data.
    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.litem_list, viewGroup,false);
        return new CourseHolder(v);
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;


        // binds the Java and XML tag together.
        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);

            //onClick on eacth item in the list.
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
     //onClick listener that gets the document repesentet in the view and x position.
    public interface onItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;

    }
}
