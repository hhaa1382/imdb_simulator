package com.example.imdp.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdp.Data.Read;
import com.example.imdp.Pages.MainPage;
import com.example.imdp.R;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.MyHolder>{
    private final RecyclerViewData[] data;
    private final RecyclerViewInterface recyclerViewInterface;

    public RecyclerViewAdaptor(RecyclerViewData[] data,RecyclerViewInterface recyclerViewInterface){
        this.data = data;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View selected=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_values_layout,parent,false);
        MyHolder holder=new MyHolder(selected,recyclerViewInterface);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        final RecyclerViewData temp=data[position];
        MainPage.position=position;
        holder.title.setText("\n"+temp.getTitle());
        holder.grade.setText("Grade : "+temp.getGrade());
        holder.time.setText("Time : "+temp.getTime());
        holder.genre.setText("Genre : "+temp.getGenre());
        holder.country.setText("Country : "+temp.getCountry());
        holder.stars.setText("Stars : "+temp.getStars());
        holder.director.setText("Directors : "+temp.getDirector());
        holder.summery.setText("\nSummery : "+temp.getSummery());
        Read.readImage(temp.getImageUrl(),holder.image);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView grade;
        public TextView time;
        public TextView genre;
        public TextView country;
        public TextView stars;
        public TextView director;
        public TextView summery;
        public ImageView image;

        public MyHolder(@NonNull View itemView , RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            title=itemView.findViewById(R.id.movie_title);
            grade=itemView.findViewById(R.id.grade);
            time=itemView.findViewById(R.id.movie_time);
            genre=itemView.findViewById(R.id.genre);
            country=itemView.findViewById(R.id.country);
            stars=itemView.findViewById(R.id.stars);
            director=itemView.findViewById(R.id.director);
            summery=itemView.findViewById(R.id.summery);
            image=itemView.findViewById(R.id.movie_image);

            itemView.setOnClickListener(e->{
                int position=getAdapterPosition();
                recyclerViewInterface.onItemClicked(position);
            });
        }
    }
}
