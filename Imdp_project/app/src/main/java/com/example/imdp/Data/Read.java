package com.example.imdp.Data;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.imdp.Movie.CastsCrews;
import com.example.imdp.Movie.CastsValue;
import com.example.imdp.Movie.MovieInfo;
import com.example.imdp.Pages.MainPage;
import com.example.imdp.R;
import com.example.imdp.RecyclerView.RecyclerViewInterface;
import com.example.imdp.RecyclerView.SetAdaptor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Read {
    private static RequestQueue queue;

    private final int readMovieInfo = 1;
    private final int readMovieList = 2;
    private final int readCastInfo = 1;
    private final int readCastList = 2;

    private final Context context;
    private final SetAdaptor adaptor;

    private int movieCounter;
    private int castCounter;

    private MainPage m;

    private final RecyclerViewInterface recyclerViewInterface;

    public Read(MainPage mainPage, SetAdaptor adaptor, RecyclerViewInterface recyclerViewInterface) {
        m=mainPage;
        this.context = m.getContext();
        this.adaptor = adaptor;
        this.recyclerViewInterface=recyclerViewInterface;
        queue=Volley.newRequestQueue(context);
    }

    public void readMovies(String url, int mode) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (mode == readMovieList) {
                            JSONArray selectedArray = response.getJSONArray("results");

                            String urlFirstPart = "https://api.themoviedb.org/3/movie/";
                            String urlSecondPart = "?api_key=078fedca37153e722fc50c43355a4fb9&language=en-US";

                            for (int i = 0; i < 10; i++) {
                                JSONObject movie = selectedArray.getJSONObject(i);
                                int id = movie.getInt("id");
                                String selectedUrl = urlFirstPart + id + urlSecondPart;
                                readMovies(selectedUrl, readMovieInfo);
                            }
                        } else if (mode == readMovieInfo) {
                            setMovieData(response);
                        }
                    } catch (JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("hamid", error.getMessage()));

        request.setRetryPolicy(new DefaultRetryPolicy(8000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void readMoviesCasts(String url, int mode, MovieInfo movie) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (mode == readCastList) {
                            JSONArray castArray = response.getJSONArray("cast");
                            JSONArray crewArray = response.getJSONArray("crew");
                            castCounter +=castArray.length();
                            castCounter +=crewArray.length();

                            for (int i = 0; i < castArray.length(); i++) {
                                JSONObject cast = castArray.getJSONObject(i);
                                String job = cast.getString("known_for_department");
                                if (job.equals("Acting")) {
                                    int id = cast.getInt("id");
                                    String datesUrl = "https://api.themoviedb.org/3/person/" + id + "?api_key=078fedca37153e722fc50c43355a4fb9&language=en-US";
                                    readMoviesCasts(datesUrl, readCastInfo, movie);
                                }
                                else {
                                    castCounter--;
                                }
                            }

                            for (int i = 0; i < crewArray.length(); i++) {
                                JSONObject crew = crewArray.getJSONObject(i);
                                String job = crew.getString("known_for_department");
                                if (job.equals("Directing") || job.equals("Writing")) {
                                    int id = crew.getInt("id");
                                    String datesUrl = "https://api.themoviedb.org/3/person/" + id + "?api_key=078fedca37153e722fc50c43355a4fb9&language=en-US";
                                    readMoviesCasts(datesUrl, readCastInfo, movie);
                                }
                                else {
                                    castCounter--;
                                }
                            }
                        }
                        else if (mode == readCastInfo) {
                            setMovieCastCrewsDate(response,movie);
                            castCounter--;
                            if (castCounter == 0) {
                                m.setRecyclerVisible();
                                adaptor.setAdaptor( Data.getMovies(),recyclerViewInterface);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("hamid", error.getMessage()));

        request.setRetryPolicy(new DefaultRetryPolicy(8000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void readCasts() {
        DynamicArray<MovieInfo> movies = Data.getMovies();
        for (int i = 0; i < movies.size(); i++) {
            int id = movies.get(i).getId();
            String value = "https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=078fedca37153e722fc50c43355a4fb9&language=en-US";
            readMoviesCasts(value, readCastList, movies.get(i));
        }
    }

    public void readMovie() throws JSONException, InterruptedException {
        if(Data.getMovies().size()==0) {
            String value = "https://api.themoviedb.org/3/movie/top_rated?api_key=078fedca37153e722fc50c43355a4fb9&language=en-US&page=";
            int pageNumber = 1;
            int movieNumber = 20;
            movieCounter = 10;

            for (int i = 1; i <= pageNumber; i++) {
                String url = value + i;
                readMovies(url, readMovieList);
            }
        }
        else{
            adaptor.setAdaptor(Data.getMovies(),recyclerViewInterface);
        }
    }

    private void setMovieCastCrewsDate(JSONObject info,MovieInfo movie) throws JSONException {
        CastsCrews c = new CastsCrews();

        int id = info.getInt("id");
        c.setPersonId(id);

        String name = info.getString("name");
        c.setName(name);

        String birthday = info.getString("birthday");
        c.setBirthYear(birthday);

        String deathday = info.getString("deathday");
        c.setDeadYear(deathday);

        String department=info.getString("known_for_department");
        if(department.equals("Acting")){
            c.setValue(CastsValue.Actor);
            movie.addActors(c);
        }
        else if(department.equals("Writing")){
            c.setValue(CastsValue.Writer);
            movie.addWriters(c);
        }
        if(department.equals("Directing")){
            c.setValue(CastsValue.Director);
            movie.addDirectors(c);
        }
    }

    private void setMovieData(JSONObject selectedObject) throws JSONException, InterruptedException {
        MovieInfo movie = new MovieInfo();

        int id = selectedObject.getInt("id");
        movie.setId(id);

        String title = selectedObject.getString("title");
        movie.setTitle(title);

        String originalTitle = selectedObject.getString("original_title");
        movie.setOriginalTitle(originalTitle);

        String region = convertJsonArrayToString(selectedObject.getJSONArray("production_countries"));
        movie.setRegion(region);

        String language = selectedObject.getString("original_language");
        movie.setLanguage(language);

        boolean isAdult = selectedObject.getBoolean("adult");
        movie.setAdult(isAdult);

        String year = selectedObject.getString("release_date");
        movie.setYear(year);

        int time = selectedObject.getInt("runtime");
        movie.setTime(time);

        String summery = selectedObject.getString("overview");
        movie.setOverView(summery);

        String genre = convertJsonArrayToString(selectedObject.getJSONArray("genres"));
        movie.setGenre(genre);

        double rating = selectedObject.getDouble("vote_average");
        movie.setRatingNumber(rating);

        int voteNum = selectedObject.getInt("vote_count");
        movie.setVoteNumber(voteNum);

        new Write(context).readRating(movie);

        String poster = selectedObject.getString("poster_path");
        String url = "https://image.tmdb.org/t/p/original/" + poster;
        movie.setImageUrl(url);

        Data.addMovie(movie);

        movieCounter--;
        if (movieCounter == 0) {
            Log.i("hamid", "Movies read");
            castCounter=0;
            readCasts();
        }
    }

    private String convertJsonArrayToString(JSONArray array) throws JSONException {
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            temp.append(object.get("name"));

            if (i != array.length() - 1) {
                temp.append(" , ");
            }
        }

        return temp.toString();
    }

    public static void readImage(String url, ImageView image) {
        Picasso.get().load(url).resize(200, 200).error(R.drawable.ic_launcher_background).into(image);
    }
}
