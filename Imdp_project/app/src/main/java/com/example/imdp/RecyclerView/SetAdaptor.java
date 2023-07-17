package com.example.imdp.RecyclerView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imdp.Data.DynamicArray;
import com.example.imdp.Movie.MovieInfo;

public class SetAdaptor {
    private int startIndex=0;
    private int endIndex=0;
    private final RecyclerView recyclerView;

    public SetAdaptor(RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
    }

    public void setAdaptor(DynamicArray<MovieInfo> data,RecyclerViewInterface recyclerViewInterface) {
        MovieInfo[] values=getSubData(data);
        RecyclerViewAdaptor adaptor = new RecyclerViewAdaptor(convertData(values),recyclerViewInterface);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
    }

    private RecyclerViewData[] convertData(MovieInfo[] values){
        RecyclerViewData[] data=new RecyclerViewData[values.length];
        for(int i=0;i<values.length;i++){
            MovieInfo m = values[i];
            RecyclerViewData temp = new RecyclerViewData();
            temp.setTitle(m.getTitle());
            temp.setGenre(m.getGenre());
            temp.setCountry(m.getRegion());
            temp.setTime(m.getTime() + " min");
            temp.setSummery(m.getOverView());
            temp.setGrade(String.valueOf(m.getRatingNumber()));
            temp.setDirector(m.getDirectorsToString());
            temp.setStars(m.getActorsToString());
            temp.setImageUrl(m.getImageUrl());
            data[i] = temp;
        }

        return data;
    }

    private MovieInfo[] getSubData(DynamicArray<MovieInfo> selectedData){
        int counter;
        if(selectedData.size()<endIndex+10){
            counter=selectedData.size()-endIndex;
            endIndex=selectedData.size();
        }
        else{
            counter=10;
            endIndex+=10;
        }

        MovieInfo[] data=new MovieInfo[endIndex-startIndex];

        for(int i=0;i<counter;i++){
            data[i]=selectedData.get(i+startIndex);
        }

        startIndex+=counter;
        return data;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
