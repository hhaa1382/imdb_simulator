package com.example.imdp.Pages;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.imdp.Account.Comment;
import com.example.imdp.Account.FavoriteList;
import com.example.imdp.Account.Person;
import com.example.imdp.Data.*;
import com.example.imdp.Movie.MovieInfo;
import com.example.imdp.R;

public class MovieInfoPage extends Fragment {
    private View view;
    private final Person person;
    private MovieInfo movie;
    private DynamicArray<MovieInfo> movies;

    private Spinner spinner;
    private DynamicArray<String> peopleValue;

    public MovieInfoPage(Person person, DynamicArray<MovieInfo> movies){
        this.movies=movies;
        this.person=person;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.movie_info,container,false);

        Bundle args=this.getArguments();
        int index=args.getInt("Index");
        movie=movies.get(index);

        initializeValue();

        ImageButton back=view.findViewById(R.id.movie_info_back_button);
        back.setOnClickListener(e -> backListener());

        Button addComment=view.findViewById(R.id.add_comment_button);
        addComment.setOnClickListener(e-> {
            if(person!=null) {
                addComment(movie);
            }
            else{
                Toast.makeText(this.getContext(),"Please do sign up or login!!",Toast.LENGTH_LONG).show();
            }
        });

        Button addRating=view.findViewById(R.id.add_rating_button);
        addRating.setOnClickListener(e->{
            if(person!=null){
                if (!person.isRated()){
                    ratingListener();
                    person.setRated();
                }
                else{
                    Toast.makeText(this.getContext(),"You rated one time",Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this.getContext(),"Please do sign up or login!!",Toast.LENGTH_LONG).show();
            }
        });

        Button showFavoriteLists=view.findViewById(R.id.show_other_favorite_lists);
        showFavoriteLists.setOnClickListener(e->{
            if(peopleValue.size()>0){
                showFavoriteList();
            }
        });

        return view;
    }

    private void ratingListener(){
        EditText ratingText=new EditText(this.getContext());
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ratingText.setLayoutParams(params);
        Context context=this.getContext();

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Enter rating : ").setTitle("Rating").setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            int rate=Integer.parseInt(ratingText.getText().toString());
                            movie.addRating(rate);
                            Write write=new Write(context);
                            write.insertValues(Write.rating,movie.getRating());

                            Toast.makeText(context,"rating added",Toast.LENGTH_SHORT).show();
                            initializeValue();
                        }
                        catch (NumberFormatException ex){
                            dialogInterface.cancel();
                            Toast.makeText(context,"You clicked wrong input",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.setView(ratingText);
        dialog.show();
    }

    private void backListener(){
        MainPage page=new MainPage(movies);

        Bundle bundle=new Bundle();
        bundle.putInt("Index",getPersonIndex());
        page.setArguments(bundle);

        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,page);
        transaction.commit();
    }

    private void showFavoriteList(){
        MainPage m=new MainPage(getFavoriteList(),movies,person);

        int index=this.getArguments().getInt("Index");

        Bundle bundle=new Bundle();
        bundle.putInt("Index",index);
        m.setArguments(bundle);

        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,m);
        transaction.commit();
    }

    private DynamicArray<Integer> getFavoriteList(){
        int index=spinner.getSelectedItemPosition();
        String[] values=peopleValue.get(index).split(" ");
        String username=values[1];
        int favoriteIndex=Integer.parseInt(values[0]);

        DynamicArray<Integer> movies=new DynamicArray<>();

        DynamicArray<Person> people = Data.getPeople();
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getUsername().equals(username)) {
                movies=people.get(i).getFavoriteLists().get(favoriteIndex).getMovies();
                break;
            }
        }

        return movies;
    }

    private int getPersonIndex(){
        if(person!=null) {
            DynamicArray<Person> people = Data.getPeople();
            for (int i = 0; i < people.size(); i++) {
                if (people.get(i).getUsername().equals(person.getUsername())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void initializeValue(){
        TextView title=view.findViewById(R.id.movie_info_title);
        title.setText(movie.getTitle());

        ImageView image=view.findViewById(R.id.movie_info_image);
        Read.readImage(movie.getImageUrl(),image);

        TextView originalTitle=view.findViewById(R.id.movie_info_original_title);
        originalTitle.setText("Original title : "+movie.getOriginalTitle());

        TextView rating=view.findViewById(R.id.movie_info_rating);
        rating.setText("Rating : "+movie.getRatingNumber());

        TextView voteNumber=view.findViewById(R.id.movie_info_vote_num);
        voteNumber.setText("Vote Number : "+movie.getVoteNumber());

        TextView genre=view.findViewById(R.id.movie_info_genre);
        genre.setText("Genre : "+movie.getGenre());

        TextView time=view.findViewById(R.id.movie_info_time);
        time.setText("Time : "+movie.getTime());

        TextView year=view.findViewById(R.id.movie_info_year);
        year.setText("Year : "+movie.getYear());

        TextView region=view.findViewById(R.id.movie_info_region);
        region.setText("Region : "+movie.getRegion());

        TextView language=view.findViewById(R.id.movie_info_language);
        language.setText("Language : "+movie.getLanguage());

        TextView directors=view.findViewById(R.id.movie_info_director);
        directors.setText("Director : "+movie.getDirectorsValues());

        TextView writer=view.findViewById(R.id.movie_info_writer);
        writer.setText("Writer : "+movie.getWritersValues());

        TextView actors=view.findViewById(R.id.movie_info_actors);
        actors.setText("Actors : "+movie.getActorsValues());

        TextView overview=view.findViewById(R.id.movie_info_overview);
        overview.setText("Overview : "+movie.getOverView());

        setComments();
        setFavoriteLists();
    }

    private void setFavoriteLists() {
        if(person!=null){
            peopleValue=new DynamicArray<>();
            String[] values=getPeopleFavoriteList();

            spinner=view.findViewById(R.id.other_favorite_lists);
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this.getContext(), R.layout.spinner_item_layout,values);
            spinner.setAdapter(adapter);
        }
    }

    private String[] getPeopleFavoriteList(){
        DynamicArray<String> values=new DynamicArray<>();
        DynamicArray<Person> people=Data.getPeople();

        for(int i=0;i<people.size();i++){
            DynamicArray<FavoriteList> favoriteLists=people.get(i).getFavoriteLists();
            for(int j=0;j<favoriteLists.size();j++){
                DynamicArray<Integer> movies=favoriteLists.get(j).getMovies();
                for(int k=0;k<movies.size();k++){
                    if(movies.get(k)==movie.getId()){
                        values.add(favoriteLists.get(j).getName()+" --- "+people.get(i).getName());
                        peopleValue.add(j+" "+people.get(i).getUsername());
                        break;
                    }
                }
            }
        }

        String[] allValues=new String[values.size()];
        for(int i=0;i<values.size();i++){
            allValues[i]=values.get(i);
        }

        return allValues;
    }

    private void addComment(MovieInfo movie){
        EditText commentText=new EditText(this.getContext());
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        commentText.setLayoutParams(params);
        Context context=this.getContext();

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Enter comment : ").setTitle("Comment").setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context,"comment added",Toast.LENGTH_SHORT).show();
                        Comment comment=new Comment(commentText.getText().toString(),person.getName(),movie.getId());
                        Write write=new Write(context);
                        write.insertValues(Write.comment,comment);
                        setComments();
                    }
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.setView(commentText);
        dialog.show();
    }

    private void setComments(){
        TextView commentsText=view.findViewById(R.id.movie_info_comments);
        StringBuilder temp=new StringBuilder();

        DynamicArray<Comment> comments=new Write(this.getContext()).readComments(movie);

        for(int i=0;i<comments.size();i++){
            Comment c=comments.get(i);
            temp.append(c.getPerson()+" :\n"+c.getComment()+"\n\n");
        }

        commentsText.setText(temp.toString());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.movie_page_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id== R.id.add_watch_list){
            addToWatchList();
            return true;
        }
        else if(id== R.id.add_favorite_movie){
            addToFavoriteList();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    private void addToWatchList(){
        person.addToWatchList(movie);
        Write write=new Write(this.getContext());
        write.updatePerson(person);
    }

    private void addToFavoriteList(){
        Context context=this.getContext();
        String[] names=getNames();

        Spinner spinner=new Spinner(context);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        spinner.setLayoutParams(params);
        spinner.setAdapter(new ArrayAdapter<>(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, names));

        AlertDialog.Builder builder=new AlertDialog.Builder(context).setTitle("Name")
                .setMessage("Choose favorite list").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(names.length>0){
                            person.getFavoriteLists().get(spinner.getSelectedItemPosition()).addMovie(movie.getId());
                            Write write=new Write(context);
                            write.updatePerson(person);
                            Toast.makeText(context,"Movie add to favorite list",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"There is no favorite list!!",Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.setView(spinner);
        dialog.show();
    }

    private String[] getNames(){
        String[] values=new String[person.getFavoriteLists().size()];
        for(int i=0;i<person.getFavoriteLists().size();i++){
            values[i]=person.getFavoriteLists().get(i).getName();
        }
        return values;
    }
}