package com.example.imdp.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.imdp.Account.Person;
import com.example.imdp.Data.Data;
import com.example.imdp.Data.DynamicArray;
import com.example.imdp.Exeptions.InvalidInput;
import com.example.imdp.R;

public class LoginPage extends Fragment {
    private EditText username;
    private EditText password;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.login_page,container,false);
        initializeValues();

        Button login=view.findViewById(R.id.login);
        login.setOnClickListener(e->{
            try {
                checkLogin();
                int selectedIndex=getPerson();
                loginListener(selectedIndex);
            }
            catch (InvalidInput ex){
                Toast.makeText(this.getContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Button back=view.findViewById(R.id.login_back);
        back.setOnClickListener(e-> backListener());

        return view;
    }

    private void backListener(){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,new MainPage());
        transaction.commit();
    }

    private void loginListener(int index){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        AccountPage account=new AccountPage();
        Bundle args=new Bundle();
        args.putInt("Index",index);
        account.setArguments(args);

        transaction.replace(R.id.main_fragment,account);
        transaction.commit();
    }

    private void initializeValues(){
        username=view.findViewById(R.id.login_person_username);
        password=view.findViewById(R.id.login_person_password);
    }

    private void checkLogin() {
        if(String.valueOf(username.getText()).isEmpty() || String.valueOf(password.getText()).isEmpty()){
            throw new InvalidInput("Import all Values");
        }
    }

    private int getPerson(){
        DynamicArray<Person> people= Data.getPeople();
        for(int i=0;i<people.size();i++){
            if(people.get(i).getUsername().equals(String.valueOf(username.getText())) &&
                    people.get(i).getPassword().equals(String.valueOf(password.getText()))){
                return i;
            }
        }
        throw new InvalidInput("Person not found");
    }
}