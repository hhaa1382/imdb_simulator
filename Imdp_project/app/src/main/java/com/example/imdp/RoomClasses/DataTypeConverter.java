package com.example.imdp.RoomClasses;

import androidx.room.TypeConverter;

import com.example.imdp.Account.FavoriteList;
import com.example.imdp.Account.WatchList;
import com.example.imdp.Data.DynamicArray;

public class DataTypeConverter {
    @TypeConverter
    public String convertWatchList(WatchList watchList){
        StringBuilder temp=new StringBuilder();
        temp.append(watchList.getPerson()+"-");

        for(int i=0;i<watchList.getMovies().size();i++){
            temp.append(watchList.getMovies().get(i));
            if(i!=watchList.getMovies().size()-1){
                temp.append(",");
            }
        }

        return temp.toString();
    }

    @TypeConverter
    public WatchList convertToWatchList(String values){
        String[] tempUser=values.split("-");
        WatchList watchList=new WatchList(tempUser[0]);

        if(tempUser.length>1) {
            String[] tempMovies = tempUser[1].split(",");
            for (String s : tempMovies) {
                watchList.addMovie(Integer.parseInt(s));
            }
        }

        return watchList;
    }

    @TypeConverter
    public String convertFavoriteList(DynamicArray<FavoriteList> favoriteLists){
        StringBuilder temp=new StringBuilder();
        for(int j=0;j<favoriteLists.size();j++) {
            FavoriteList hold=favoriteLists.get(j);
            temp.append(hold.getPerson() + "-"+hold.getName()+"-");

            for (int i = 0; i < hold.getMovies().size(); i++) {
                temp.append(hold.getMovies().get(i));
                if (i != hold.getMovies().size() - 1) {
                    temp.append(",");
                }
            }

            if(j!=favoriteLists.size()-1){
                temp.append("@");
            }
        }

        return temp.toString();
    }

    @TypeConverter
    public DynamicArray<FavoriteList> convertToFavoriteList(String values){
        DynamicArray<FavoriteList> favoriteLists=new DynamicArray<>();

        if(!values.isEmpty()) {
            String[] tempFavoriteLists = values.split("@");

            for (String str : tempFavoriteLists) {
                String[] tempUser = str.split("-");
                FavoriteList temp = new FavoriteList(tempUser[0],tempUser[1]);

                if(tempUser.length>2) {
                    String[] tempMovies = tempUser[2].split(",");
                    for (String s : tempMovies) {
                        temp.addMovie(Integer.parseInt(s));
                    }
                }

                favoriteLists.add(temp);
            }
        }

        return favoriteLists;
    }
}
