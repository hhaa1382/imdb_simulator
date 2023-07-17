package com.example.imdp.Pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.imdp.Account.FavoriteList;
import com.example.imdp.Account.Person;
import com.example.imdp.Data.Data;
import com.example.imdp.Data.DynamicArray;
import com.example.imdp.Data.Write;
import com.example.imdp.Movie.MovieInfo;
import com.example.imdp.R;

import java.util.Random;

public class AccountPage extends Fragment {
    private Person person;
    private View view;
    private Spinner spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.account_layout,container,false);

        checkPerson();
        TextView txtName=view.findViewById(R.id.welcome_text);
        txtName.setText("Welcome "+person.getName());
        setFavoriteList();

        Button movies=view.findViewById(R.id.movies_button);
        movies.setOnClickListener(e->moviePage(null));

        Button btnWatchList=view.findViewById(R.id.watch_list_button);
        btnWatchList.setOnClickListener(e->{
            if(person.getWatchList().getMovies().size()==0){
                Toast.makeText(this.getContext(),"Watch list is empty!!",Toast.LENGTH_SHORT).show();
            }
            else{
                moviePage(person.getWatchList().getMovies());
            }
        });

        Button btnAddFavoriteList=view.findViewById(R.id.add_favorite_list_button);
        btnAddFavoriteList.setOnClickListener(e-> addFavoriteList());

        Button btnShowFavoriteList=view.findViewById(R.id.show_favorite_movies_button);
        btnShowFavoriteList.setOnClickListener(e->{
            int index=spinner.getSelectedItemPosition();
            if(person.getFavoriteLists().get(index).getMovies().size()>0) {
                moviePage(person.getFavoriteLists().get(index).getMovies());
            }
            else Toast.makeText(this.getContext(),"Favorite list is empty!!",Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void checkPerson() {
        Bundle args=this.getArguments();
        person= Data.getPeople().get(args.getInt("Index"));
    }

    private void moviePage(DynamicArray<Integer> values){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        MainPage m;
        if(values!=null) {
             m= new MainPage(values,null,null);
        }
        else {
            m= new MainPage();
            m.dataChecked(true);
        }

        Bundle tempArgs=this.getArguments();
        int index=tempArgs.getInt("Index");

        Bundle args=new Bundle();
        args.putInt("Index",index);
        m.setArguments(args);

        transaction.replace(R.id.main_fragment,m);
        transaction.commit();
    }

    private void setFavoriteList(){
        spinner=view.findViewById(R.id.favorite_lists_name);
        String[] values=getFavoriteListNames();
        Context context=this.getContext();

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context, R.layout.spinner_item_layout,values);
        spinner.setAdapter(arrayAdapter);
    }

    private String[] getFavoriteListNames(){
        String[] temp=new String[person.getFavoriteLists().size()];
        for(int i=0;i<temp.length;i++){
            temp[i]=person.getFavoriteLists().get(i).getName();
        }
        return temp;
    }

    private void addFavoriteList(){
        Context context=this.getContext();

        EditText nameText=new EditText(context);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        nameText.setLayoutParams(params);

        AlertDialog.Builder builder=new AlertDialog.Builder(context).setTitle("Name")
                .setMessage("Enter list name :").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!nameText.getText().toString().isEmpty()) {
                            person.addFavorite(new FavoriteList(person.getUsername(), nameText.getText().toString()));
                            Write write=new Write(context);
                            write.updatePerson(person);
                            setFavoriteList();

                            Toast.makeText(context,"Favorite list added",Toast.LENGTH_SHORT).show();
                        }
                        else dialogInterface.cancel();
                    }
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.setView(nameText);
        dialog.show();
    }

    private void offerListener() {
        Random random=new Random();
        int randomNumber=random.nextInt(2);
        if(randomNumber==0){
            String genre=getGenre();
            setMovieWithGenre(genre);
        }
        if(randomNumber==1){
            int value=random.nextInt(5)+4;
            setMovieWithGrade(value);
        }
    }

    private void setMovieWithGrade(int grade){
        DynamicArray<MovieInfo> newMovies=new DynamicArray<>();
        DynamicArray<MovieInfo> movies=Data.getMovies();

        for(int i=0;i<movies.size();i++){
            if(movies.get(i).getRating().getNumber()>=grade && movies.get(i).getRating().getNumber()<=grade+1){
                newMovies.add(movies.get(i));
            }
        }

        offerMoviePage(newMovies);
    }

    private void setMovieWithGenre(String genre){
        DynamicArray<MovieInfo> newMovies=new DynamicArray<>();
        DynamicArray<MovieInfo> movies=Data.getMovies();

        for(int i=0;i<movies.size();i++){
            if(movies.get(i).getGenre().contains(genre)){
                newMovies.add(movies.get(i));
            }
        }

        offerMoviePage(newMovies);
    }

    private void offerMoviePage(DynamicArray<MovieInfo> movies){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        MainPage m=new MainPage(movies);
        Bundle tempArgs=this.getArguments();
        int index=tempArgs.getInt("Index");

        Bundle args=new Bundle();
        args.putInt("Index",index);
        m.setArguments(args);

        transaction.replace(R.id.main_fragment,m);
        transaction.commit();
    }

    private String getGenre(){
        String[] genres={"Drama","Crime","History","War","Comedy","Romance","Animation","Family","Fantasy"};

        Random rand=new Random();
        int index=rand.nextInt(genres.length);
        return genres[index];
    }

    private DynamicArray<MovieInfo> getBestMovies(DynamicArray<MovieInfo> movies){
        DynamicArray<MovieInfo> selectedMovies=new DynamicArray<>(10);

        for(int i=0;i<movies.size();i++){
            for(int j=0;j<movies.size()-1;j++){
                if(movies.get(j).compareTo(movies.get(j+1))<0){
                    MovieInfo temp=movies.get(j);
                    movies.replace(movies.get(j+1),j);
                    movies.replace(temp,j+1);
                }
            }
        }

        for(int i=0;i<10;i++){
            selectedMovies.add(movies.get(i));
        }

        return selectedMovies;
    }

    private void bestMoviesListener() {
        DynamicArray<MovieInfo> sortedMovies=Data.getMovies().clone();
        sortedMovies=getBestMovies(sortedMovies);
        offerMoviePage(sortedMovies);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.offer_movie){
            offerListener();
            return true;
        }

        if(item.getItemId()== R.id.best_movies){
            bestMoviesListener();
            return true;
        }

        else if(item.getItemId()== R.id.logout){
            FragmentManager fragmentManager=this.getParentFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.replace(R.id.main_fragment,new MainPage());
            transaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
