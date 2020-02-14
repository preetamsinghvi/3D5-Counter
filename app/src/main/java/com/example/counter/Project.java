package com.example.counter;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Project implements Serializable {

    private String name;
    private int counter;
    private String description;
    //private double progress; //*100 is progress % maybe make into int?

    Project(String pName, String pDescription) {
        if(pName == null || pDescription == null){
            throw new IllegalArgumentException("Input not acceptable");
        }
        if(pDescription.isEmpty() || pName.isEmpty()){
            throw new IllegalArgumentException("Input String is empty");
        }
        name = pName;
        description = pDescription;
        counter = 0;
    }

    Project(String pName, String pDescription, int pRows) {
        if(pName == null || pName.isEmpty()|| pDescription == null || pDescription.isEmpty()|| counter<0){
            throw new IllegalArgumentException("Input not acceptable");
        }
        name = pName;
        description = pDescription;
        counter = pRows;
    }

    String getName(){
        return name;
    }

    String getDescription(){
        return description;
    }

    int getCounter(){
        return counter;
    }

    void setName(String pName) {
        if(pName == null || pName.isEmpty()){
            throw new IllegalArgumentException("NO setName!");
        }
        name = pName;
    }

    void setDescription(String pDesc){
        if(pDesc == null || pDesc.isEmpty()){
            throw new IllegalArgumentException("NO setDesc");
        }
        description = pDesc;
    }

    void setCounter(int pCounter){
        if (pCounter < 0) {
            throw new IllegalArgumentException("Counter can't be lower than 0");
        }
        counter = pCounter;
    }


    //TODO: Override CompareTo or Equals method
}

