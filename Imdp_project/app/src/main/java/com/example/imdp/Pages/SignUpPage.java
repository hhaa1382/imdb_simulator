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

import java.util.regex.Pattern;

import com.example.imdp.Account.Person;
import com.example.imdp.Data.*;
import com.example.imdp.Exeptions.InvalidInput;
import com.example.imdp.R;

public class SignUpPage extends Fragment {
    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText username;
    private EditText password;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.sign_up_page,container,false);

        initializeValues();

        Button signUp=view.findViewById(R.id.sign_up);
        signUp.setOnClickListener(e->{
            String message="";
            try {
                checkSignUp();
                Person person=new Person(String.valueOf(name.getText()),String.valueOf(phone.getText())
                        ,String.valueOf(email.getText()),String.valueOf(username.getText()),String.valueOf(password.getText()));
                Data.addPerson(person);
                Write write=new Write(this.getContext());
                write.insertValues(Write.person,person);
                signUpListener();

                message="Person add successfully";
            }
            catch (InvalidInput ex){
                message=ex.getMessage();
            }
            finally {
                Toast.makeText(this.getContext(),message,Toast.LENGTH_LONG).show();
            }
        });

        Button back=view.findViewById(R.id.sign_up_back);
        back.setOnClickListener(e->{
            backListener();
        });

        return view;
    }

    private void signUpListener(){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        MainPage m=new MainPage();
        Bundle args=new Bundle();
        args.putInt("Index",Data.getPeople().size()-1);
        m.setArguments(args);

        transaction.replace(R.id.main_fragment,m);
        transaction.commit();
    }

    private void backListener(){
        FragmentManager fragmentManager=this.getParentFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment,new MainPage());
        transaction.commit();
    }

    private void initializeValues(){
        name=view.findViewById(R.id.sign_up_person_name);
        phone=view.findViewById(R.id.sign_up_person_phone);
        email=view.findViewById(R.id.sign_up_person_email);
        username=view.findViewById(R.id.sign_up_person_username);
        password=view.findViewById(R.id.sign_up_person_password);
    }

    private void checkSignUp(){
        if(String.valueOf(name.getText()).isEmpty() || String.valueOf(phone.getText()).isEmpty() || String.valueOf(email.getText()).isEmpty()
        || String.valueOf(username.getText()).isEmpty() || String.valueOf(password.getText()).isEmpty()){
            throw new InvalidInput("Import all the values");
        }
        else{
            checkEmail(String.valueOf(email.getText()));
            checkPhone(String.valueOf(phone.getText()));
            checkUsername(String.valueOf(username.getText()));
            checkPassword(String.valueOf(password.getText()));
            isUsernameUnique(String.valueOf(username.getText()));
        }
    }

    private void checkEmail(String email){
        if(!Pattern.matches("^(.+)@(.+)$",email)){
            throw new InvalidInput("Invalid email!!!");
        }
    }

    private void checkPhone(String phone){
        if(!Pattern.matches("[0-9]{9}",phone)){
            throw new InvalidInput("Invalid phone!!!");
        }
    }

    private void checkUsername(String username){
        if(!Pattern.matches("^(.){8,}",username)){
            throw new InvalidInput("Invalid username!!!");
        }
    }

    private void checkPassword(String password){
        if(!Pattern.matches("^(.){8,}",password)){
            throw new InvalidInput("Invalid password!!!");
        }
    }

    private void isUsernameUnique(String username){
        DynamicArray<Person> people=Data.getPeople();
        for(int i=0;i<people.size();i++){
            if(people.get(i).getUsername().equals(String.valueOf(username))){
                throw new InvalidInput("Username has used");
            }
        }
    }
}