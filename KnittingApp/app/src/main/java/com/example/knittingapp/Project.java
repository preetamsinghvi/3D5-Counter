package com.example.knittingapp;

public class Project {

        private String name;
        private int counter;
        private String description;
        //private double progress; //*100 is progress % maybe make into int?

        //TODO: Check Input and throw Exceptions
        Project(String pName, String pDescription) {
            name = pName;
            description = pDescription;
            counter = 0;
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

        //TODO: Check Input
        void setName(String pName){
            name = pName;
        }

        //TODO: Check Input
        void setDescription(String pDesc){
            description = pDesc;
        }

        //TODO: Check Input
        void setCounter(int pCounter){
            counter = pCounter;
        }
}
