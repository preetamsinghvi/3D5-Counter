package com.example.knittingapp;

public class Project {

        private String name;
        private int counter;
        private String description;
        //private double progress; //*100 is progress % maybe make into int?

        //TODO: Check Input and throw Exceptions
        Project(String pName, String pDescription) {
            try {
                name = pName;
                description = pDescription;
                counter = 0;
            } catch(Exception e) { }
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
            try {
                name = pName;
            } catch(Exception e) { }

        //TODO: Check Input
        void setDescription(String pDesc){
            try {
                description = pDesc;
            } catch(Exception e) { }
        }

        //TODO: Check Input
        void setCounter(int pCounter){
            try {
                counter = pCounter;
            } catch(Exception e) { }

        }
}
