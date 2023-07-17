package com.example.imdp.Data;

import androidx.annotation.NonNull;

public class DynamicArray<E> {
    private E[] values;
    private int size;

    public DynamicArray() {
        this(200);
    }

    public DynamicArray(int size) {
        values=(E[]) new Object[size];
    }

    public int size(){
        return size;
    }

    public void add(E element){
        if(size==values.length){
            addShift();
        }
        values[size++]=element;
    }

    public E get(int index){
        if(index<0 || index>size-1){
            throw new ArrayIndexOutOfBoundsException();
        }
        return values[index];
    }

    public E remove(int index){
        if(index<0 || index>size-1){
            throw new ArrayIndexOutOfBoundsException();
        }

        E selected=values[index];
        removeShift(selected);
        size--;
        return selected;
    }

    public E remove(E element){
        if(element==null){
            throw new NullPointerException();
        }

        E selected=null;
        for(int i=0;i<size;i++){
            if(element==values[i]){
                selected=values[i];
                break;
            }
        }

        if(selected!=null) {
            removeShift(selected);
            size--;
        }
        return selected;
    }

    public void addShift(){
        E[] newValues=(E[])new Object[values.length*2];
        for(int i=0;i<values.length;i++){
            newValues[i]=values[i];
        }
        values=newValues;
    }

    public void removeShift(E selected){
        E[] newValues=(E[])new Object[values.length-1];
        int counter=0;

        for(int i=0;i<values.length;i++){
            if(values[i]!=selected){
                newValues[counter]=values[i];
                counter++;
            }
        }

        values=newValues;
    }

    public void replace(E element,int index){
        if(index<0 || index>size-1){
            throw new ArrayIndexOutOfBoundsException();
        }
        values[index]=element;
    }

    @NonNull
    public DynamicArray<E> clone(){
        DynamicArray<E> temp=new DynamicArray<>(this.size);
        for(int i=0;i<this.size;i++){
            temp.add(values[i]);
        }
        return temp;
    }
}
