package com.example.irisind.companyChat.Controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.irisind.companyChat.Models.Company;
import com.example.irisind.companyChat.Models.User;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by irisind on 2/5/17.
 */

public class Session {
    private static final String TAG = "Session";
    private Application application;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    private String userType;



    public void setUser(User user) {
        if (user == null){
            loginStatus = LoginStatus.NOT_LOGIN;
            return;
        }

        else {
            this.user = user;
            loginStatus = LoginStatus.LOGIN_SUCCESS;
        }

        EventBus.getDefault().post(loginStatus);

    }

    private User user;

    public void setCompany(Company company) {
        if (company == null){
            loginStatus = LoginStatus.NOT_LOGIN;
            return;
        }

        else {
            this.company = company;
            loginStatus = LoginStatus.LOGIN_SUCCESS;
        }

        EventBus.getDefault().post(loginStatus);
    }

    private Company company;
    private static Session ourInstance;
    public enum LoginStatus {LOGIN_IN_PROGRESS, LOGIN_SUCCESS, NOT_LOGIN,LOGOUT_FAIL};


    private LoginStatus loginStatus = LoginStatus.LOGIN_IN_PROGRESS;

    public static Session getInstance() {
        return ourInstance;
    }

    public static LoginStatus getLoginStatus() {
        return ourInstance.loginStatus;
    }
    public static Session createInstance(Application application) {
        if (ourInstance == null)
            ourInstance = new Session(application);
        Log.e("Session","getting session instance");
        return ourInstance;
    }

    private Session(Application application) {
        this.application = application;
        Log.e("Session","Session created");
        checkLoginStatus();
    }

    public void checkLoginStatus() {
        Log.e(TAG,"Checking login status");
        loginStatus = LoginStatus.LOGIN_IN_PROGRESS;
        EventBus.getDefault().post(loginStatus);
        Log.e(TAG,"loginstatus = "+loginStatus.name());


    }

    /*public void reloadUser(){
        if(user == null) return;

        Log.e(TAG, "Reloading user");
        getUser(application, user.getId());
    }*/

    public void logout(Context context) {
        Log.e(TAG, "login status = " + loginStatus);

        if (loginStatus == LoginStatus.LOGIN_SUCCESS){

            Session.this.user = null;
            Session.this.company = null;
            clearApplicationData();
            application.clearCookies();
            application.clearSharedPreferences(context);
            loginStatus = LoginStatus.NOT_LOGIN;
            EventBus.getDefault().post(loginStatus);
            Toast.makeText(application, "Successfully logged out", Toast.LENGTH_SHORT).show();


        }
        else{
            loginStatus = LoginStatus.LOGOUT_FAIL;
            EventBus.getDefault().post(loginStatus);

        }

    }

   /* private void getUser(final Application application, String id) {

    }*/

    public User getUser() {
        return user;
    }

    public Company getCompany(){
        return company;
    }

    public void clearApplicationData() {
        File cache = application.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }


}
