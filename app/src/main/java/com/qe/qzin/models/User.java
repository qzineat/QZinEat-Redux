package com.qe.qzin.models;

import com.parse.ParseUser;

/**
 * Created by sheetal on 10/21/16.
 */

public class User extends ParseUser {



    private String phone ;

    public User() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static boolean isLoggedIn(){
        ParseUser currentUser = User.getCurrentUser();
        if(currentUser != null){
            return true;
        }
        return  false;
    }
}
