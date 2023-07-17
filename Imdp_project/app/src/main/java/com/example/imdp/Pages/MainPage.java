package com.example.imdp.Pages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import com.example.imdp.Account.Person;
import com.example.imdp.Data.*;
import com.example.imdp.Movie.MovieInfo;
import com.example.imdp.R;
import com.example.imdp.RecyclerView.RecyclerViewInterface;
import com.example.imdp.RecyclerView.SetAdaptor;

public class MainPage extends Fragment implements RecyclerViewInterface {
    private Read read;
    private SetAdaptor adaptor;
    private Person person=null;

    private DynamicArray<MovieInfo> movies;
    private DynamicArray<Integer> movieId;

    private boolean data=false;

    private DynamicArray<MovieInfo> infoPageBack;

    private View view;

    private RecyclerView recyclerView;
    private TextView textView;

    private int yPosition=0;
    public static int position;

    public MainPage(DynamicArray<Integer> movies, DynamicArray<MovieInfo> infoPageBack, Person person){
        data=true;

        this.person=person;
        this.infoPageBack=infoPageBack;
        movieId=movies;
    }

    public MainPage(){}

    public MainPage(DynamicArray<MovieInfo> movies){
        data=true;
        this.movies=movies;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.main_page,container,false);

        checkPerson();
        filterPartInitialize();

        adaptor=new SetAdaptor(view.findViewById(R.id.movie_lists));
        read=new Read(this,adaptor,this);

        RelativeLayout relativeLayout=view.findViewById(R.id.main_page_layout);
        recyclerView=view.findViewById(R.id.movie_lists);
        if(!data) {
            textView=new TextView(this.getContext());
            recyclerView.setVisibility(View.INVISIBLE);

            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW,R.id.filter_part);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(0,100,0,0);

            textView.setVisibility(View.VISIBLE);
            textView.setLayoutParams(params);
            textView.setText("LOADING...");
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(36);
            relativeLayout.addView(textView);
            readData();
        }
        else{
            setData();
        }

        LinearLayout l=view.findViewById(R.id.filter_part);

        ImageButton search=view.findViewById(R.id.search_button);
        search.setOnClickListener(e->{
            EditText searchEditText=view.findViewById(R.id.search_edit_text);
            checkSearch(String.valueOf(searchEditText.getText()));
        });

        Button nextPage=view.findViewById(R.id.next_page_button);
        nextPage.setOnClickListener(e-> nextPageListener());

        Button previousPage=view.findViewById(R.id.previous_page_button);
        previousPage.setOnClickListener(e->previousPageListener());

        Button setFilterButton=view.findViewById(R.id.set_filter_button);
        setFilterButton.setOnClickListener(e-> setFilterButtonListener());

        Button takeFilterButton=view.findViewById(R.id.take_filter_button);
        takeFilterButton.setOnClickListener(e-> takeFilterButtonListener());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                yPosition+=dy;
                if(dy>0 && yPosition>=100){
                    l.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.search_edit_text);
                    recyclerView.setLayoutParams(params);
                }
                else if(dy<0 && yPosition<=100){
                    l.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.filter_part);
                    recyclerView.setLayoutParams(params);
                }
                super.onScrolled(recyclerView,dx,dy);
            }
        });

        return view;
    }

    public void setRecyclerVisible(){
        recyclerView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    private void checkPerson() {
        if(person==null) {
            Bundle args = this.getArguments();
            if (args != null) {
                int index = args.getInt("Index");
                if (index != -1) {
                    person = Data.getPeople().get(index);
                }
            }
        }
    }

    private void sortButtonListener(){
        checkMovies();
        DynamicArray<MovieInfo> sortedMovies=movies.clone();

        sort(sortedMovies);

        adaptor.setStartIndex(0);
        adaptor.setEndIndex(0);
        adaptor.setAdaptor(sortedMovies,this);
        movies=sortedMovies;
    }

    private void sort(DynamicArray<MovieInfo> movies){
        for(int i=0;i<movies.size();i++){
            for(int j=0;j<movies.size()-1;j++){
                if(movies.get(j).compareTo(movies.get(j+1))<0){
                    MovieInfo temp=movies.get(j);
                    movies.replace(movies.get(j+1),j);
                    movies.replace(temp,j+1);
                }
            }
        }
    }

    private void showMovies(DynamicArray<MovieInfo> movies){
        for(int i=0;i<movies.size();i++){
            Log.i("hamid", "Rating : "+movies.get(i).getRating()+"   Year : "+movies.get(i).getYear());
        }
    }

    private void setFilterButtonListener(){
        EditText yearText=view.findViewById(R.id.year_filter);
        EditText genreText=view.findViewById(R.id.genre_filter);
        EditText starText=view.findViewById(R.id.stars_filter);
        EditText countryText=view.findViewById(R.id.country_filter);

        CheckBox yearCheck=view.findViewById(R.id.year_check);
        CheckBox genreCheck=view.findViewById(R.id.genre_check);
        CheckBox starCheck=view.findViewById(R.id.stars_check);
        CheckBox countryCheck=view.findViewById(R.id.country_check);

        int year=0;
        String genre="",star="",country="";

        if(yearCheck.isChecked()){
            year=Integer.parseInt(yearText.getText().toString());
            Log.i("Info_read",String.valueOf(year));
        }

        if(genreCheck.isChecked()){
            genre=genreText.getText().toString();
        }

        if(starCheck.isChecked()){
            star=starText.getText().toString();
        }

        if(countryCheck.isChecked()){
            country=countryText.getText().toString();
        }

        checkMovies();
        DynamicArray<MovieInfo> filterMovies=new DynamicArray<>();

        for(int i=0;i<movies.size();i++){
            boolean check=movies.get(i).checkFilter(year,genre,star,country);
            if(check){
                filterMovies.add(movies.get(i));
            }
        }

        adaptor.setStartIndex(0);
        adaptor.setEndIndex(0);
        adaptor.setAdaptor(filterMovies,this);
        movies=filterMovies;
    }

    private void takeFilterButtonListener(){
        adaptor.setStartIndex(0);
        adaptor.setEndIndex(0);
        adaptor.setAdaptor(Data.getMovies(),this);
    }

    private void filterPartInitialize(){
        EditText yearText=view.findViewById(R.id.year_filter);
        yearText.setEnabled(false);

        CheckBox yearCheck=view.findViewById(R.id.year_check);
        yearCheck.setOnClickListener(e-> yearText.setEnabled(yearCheck.isChecked()));

        EditText genreText=view.findViewById(R.id.genre_filter);
        genreText.setEnabled(false);

        CheckBox genreCheck=view.findViewById(R.id.genre_check);
        genreCheck.setOnClickListener(e-> genreText.setEnabled(genreCheck.isChecked()));

        EditText starText=view.findViewById(R.id.stars_filter);
        starText.setEnabled(false);

        CheckBox starCheck=view.findViewById(R.id.stars_check);
        starCheck.setOnClickListener(e-> starText.setEnabled(starCheck.isChecked()));

        EditText countryText=view.findViewById(R.id.country_filter);
        countryText.setEnabled(false);

        CheckBox countryCheck=view.findViewById(R.id.country_check);
        countryCheck.setOnClickListener(e-> countryText.setEnabled(countryCheck.isChecked()));
    }

    private void previousPageListener() {
        if(adaptor.getStartIndex()>0){
            if(adaptor.getStartIndex()>20) {
                adaptor.setStartIndex(adaptor.getStartIndex() - 20);
                adaptor.setEndIndex(adaptor.getStartIndex());
            }
            else{
                adaptor.setEndIndex(0);
                adaptor.setStartIndex(0);
            }
            checkMovies();
            adaptor.setAdaptor(movies,this);
        }
    }

    private void nextPageListener(){
        DynamicArray<MovieInfo> data=Data.getMovies();
        if(adaptor.getStartIndex()<data.size()){
            checkMovies();
            adaptor.setAdaptor(movies,this);
        }
    }

    private void readData(){
        Thread t=new Thread(()->{
            try {
                read.readMovie();
            }
            catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private void setData(){
        if(movieId!=null) {
            movies = new DynamicArray<>();

            DynamicArray<MovieInfo> allMovies = Data.getMovies();
            for (int i = 0; i < movieId.size(); i++) {
                for (int j = 0; j < allMovies.size(); j++) {
                    if (movieId.get(i) == allMovies.get(j).getId()) {
                        movies.add(allMovies.get(j));
                        break;
                    }
                }
            }
        }
        else {
            movies=Data.getMovies();
        }

        adaptor.setAdaptor(movies,this);
    }

    private void signUpListener(){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,new SignUpPage());
        transaction.commit();
    }

    private void loginListener(){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,new LoginPage());
        transaction.commit();
    }

    private void backListener(){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        if(infoPageBack==null) {
            AccountPage a = new AccountPage();

            int index = this.getArguments().getInt("Index");
            Bundle b = new Bundle();
            b.putInt("Index", index);
            a.setArguments(b);

            transaction.replace(R.id.main_fragment, a);
            transaction.commit();
        }
        else{
            MovieInfoPage movieInfoPage =new MovieInfoPage(person,infoPageBack);

            int index = this.getArguments().getInt("Index");
            Bundle b = new Bundle();
            b.putInt("Index", index);
            movieInfoPage.setArguments(b);

            transaction.replace(R.id.main_fragment, movieInfoPage);
            transaction.commit();
        }
    }

    private void checkSearch(String searchText){
        checkMovies();
        DynamicArray<MovieInfo> newMovies=new DynamicArray<>(100);

        for(int i=0;i<movies.size();i++){
            MovieInfo m=movies.get(i);
            if (m.getTitle().contains(searchText) || m.getOriginalTitle().contains(searchText)) {
                newMovies.add(m);
            }
        }

        adaptor.setStartIndex(0);
        adaptor.setEndIndex(0);
        adaptor.setAdaptor(newMovies,this);
        movies=newMovies;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getActorsDate(){
        DynamicArray<String> values=new DynamicArray<>();
        DynamicArray<MovieInfo> movies=Data.getMovies();

        for(int i=0;i<movies.size();i++){
            for(int j=0;j<movies.get(i).getActors().size();j++){
                if(movies.get(i).getActors().get(j).checkDate()){
                    if(!checkRepetition(values,movies.get(i).getActors().get(j).getName())) {
                        values.add(movies.get(i).getActors().get(j).getName());
                    }
                }
            }
        }

        StringBuilder builder=new StringBuilder();
        for(int i=0;i<values.size();i++){
            builder.append(values.get(i)).append("\n");
        }

        return builder.toString();
    }

    private boolean checkRepetition(DynamicArray<String> values,String value){
        for(int i=0;i<values.size();i++){
            if (values.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void actorsDateListener(){
        String values=getActorsDate();
        Context context=this.getContext();

        ScrollView scrollView=new ScrollView(context);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(params);

        AlertDialog.Builder builder=new AlertDialog.Builder(context).setTitle("Actors").
                setMessage(values).setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog=builder.create();
        dialog.setView(scrollView);
        dialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        if(person==null){
            inflater.inflate(R.menu.main_page_menu,menu);
        }
        else{
            inflater.inflate(R.menu.movie_list_menu,menu);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(person==null) {
            switch (id) {
                case R.id.sign_up_button:
                    signUpListener();
                    return true;

                case R.id.log_in_button:
                    loginListener();
                    return true;

                case R.id.exit:
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        else{
            switch (id) {
                case R.id.show_actor_date:
                    actorsDateListener();
                    return true;

                case R.id.sort_button:
                    checkMovies();
                    sortButtonListener();
                    return true;

                case R.id.movie_list_part_back:
                    backListener();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        checkMovies();
        MovieInfoPage p=new MovieInfoPage(person,movies);
        Bundle args=new Bundle();
        args.putInt("Index",position);
        p.setArguments(args);

        transaction.replace(R.id.main_fragment,p);
        transaction.commit();
    }

    private void checkMovies(){
        if(movies==null){
            movies=Data.getMovies();
        }
    }

    public void dataChecked(boolean data){
        this.data=data;
    }
}
