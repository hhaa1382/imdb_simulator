package com.example.imdp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.imdp.Account.FavoriteList;
import com.example.imdp.Account.Person;
import com.example.imdp.Data.Data;
import com.example.imdp.Pages.MainPage;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Person person=new Person("h","h","h","h","h");
        person.addFavorite(new FavoriteList("h","like1"));

        Data.addPerson(person);
//        new Write(this).insertValues(Write.person,new Person("hh","hh","hh","hh","11"));

        for(int i=0;i<Data.getPeople().size();i++){
            if(Data.getPeople().get(i).getUsername().equals("hh")){
//                Log.i("hamid", "Size : "+Data.getPeople().get(i).getFavoriteLists().size());
            }
            Log.i("hamid", "Username : "+Data.getPeople().get(i).getUsername()+"   Password : "+Data.getPeople().get(i).getPassword());
        }

        setFragment();
    }

    private void setFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,new MainPage());
        transaction.commit();
    }
}