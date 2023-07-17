package com.example.imdp.Movie;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class CastsCrews {
    private int personId;
    private String name;
    private String birthYear;
    private String deadYear;
    private CastsValue value;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getDeadYear() {
        return deadYear;
    }

    public void setDeadYear(String deadYear) {
        this.deadYear = deadYear;
    }

    public CastsValue getValue() {
        return value;
    }

    public void setValue(CastsValue value) {
        this.value = value;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkDate(){
        LocalDate now=LocalDate.now();
        if(!birthYear.equals("null")){
            return getBirthMonth() == now.getMonthValue() && getBirthDay() == now.getDayOfMonth();
        }
        return false;
    }

    private int getBirthMonth(){
        String[] values=birthYear.split("-");
        return Integer.parseInt(values[1]);
    }

    private int getBirthDay(){
        String[] values=birthYear.split("-");
        return Integer.parseInt(values[2]);
    }
}
