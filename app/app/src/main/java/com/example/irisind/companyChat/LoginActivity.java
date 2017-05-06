package com.example.irisind.companyChat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.irisind.companyChat.Controllers.Application;
import com.example.irisind.companyChat.Controllers.Session;
import com.example.irisind.companyChat.Models.Company;
import com.example.irisind.companyChat.Models.User;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Application app;
    private EditText input_phone;
    private TextView link_signup;
    private Button btn_login, user, company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e(TAG, "created");
        app = (Application) getApplication();

        input_phone = (EditText) findViewById(R.id.input_phone);
        link_signup = (TextView) findViewById(R.id.link_signup);
        btn_login = (Button) findViewById(R.id.btn_login);
        user = (Button) findViewById(R.id.user);
        company = (Button) findViewById(R.id.company);

        user.setOnTouchListener(new userTypeOnTouch());
        company.setOnTouchListener(new userTypeOnTouch());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_phone.getText().length() < 6) {
                    Toast.makeText(LoginActivity.this, "Enter valid phonenumber", Toast.LENGTH_SHORT).show();

                } else if (!isUserTypeSelected()) {
                    Toast.makeText(LoginActivity.this, "select who are you", Toast.LENGTH_SHORT).show();
                } else {


                    if (user.isPressed()) {
                        userLogin(input_phone.getText().toString());
                    } else if (company.isPressed()) {
                        companyLogin(input_phone.getText().toString());
                    }


                }

            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isPressed()) {
                    Intent intent = new Intent(LoginActivity.this, EditUserProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("TITLE", "Signup as User");
                    startActivity(intent);
                } else if (company.isPressed()) {
                    Intent intent = new Intent(LoginActivity.this, EditCompanyProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("TITLE", "Signup as Company");
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Select user/company and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isUserTypeSelected() {
        return user.isPressed() || company.isPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        SharedPreferences sharedPreferences = Application.getSharedPrefrence(LoginActivity.this);
        String phone = sharedPreferences.getString("phoneNumber", null);
        String type = sharedPreferences.getString("userType", null);
        if (phone != null && type != null) {
            if (type.equals("user")) {
                userLogin(phone);
            } else if (type.equals("company")) {
                companyLogin(phone);
            }
            Log.e(TAG, phone);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Session.LoginStatus loginStatus) {

        Log.e("LoginActivity", loginStatus.name());
        if (loginStatus == Session.LoginStatus.NOT_LOGIN) {

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        if (loginStatus == Session.LoginStatus.LOGIN_SUCCESS) {

            String userType = Session.getInstance().getUserType();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            finish();

            /*if (userType.equals("user")){
                if (Session.getInstance().getUser().isProfileComplete()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    finish();
                } else {

                }
            } else if (userType.equals("company")){
                if (Session.getInstance().getCompany().isProfileComplete()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(intent);
                    finish();
                }else {

                }
            }*/


        }

    }

    public void userLogin(String phone) {
        JsonObject j1 = new JsonObject();
        j1.addProperty("phoneNumber", phone);
        JsonObject filter = new JsonObject();
        filter.add("where", j1);

        app = (Application) getApplication();
        app.getApi().getUser(filter.toString()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    Session.getInstance().setUserType("user");
                    Session.getInstance().setUser(response.body());
                    Application.saveToSharedPreferences(LoginActivity.this, response.body().getPhoneNumber(), "user");

                } else {
                    Toast.makeText(LoginActivity.this, "No user found. Please signup first", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void companyLogin(String phone) {
        JsonObject j1 = new JsonObject();
        j1.addProperty("phoneNumber", phone);
        JsonObject filter = new JsonObject();
        filter.add("where", j1);

        app = (Application) getApplication();
        app.getApi().getCompany(filter.toString()).enqueue(new Callback<Company>() {
            @Override
            public void onResponse(Call<Company> call, Response<Company> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    Session.getInstance().setUserType("company");
                    Session.getInstance().setCompany(response.body());
                    Application.saveToSharedPreferences(LoginActivity.this, response.body().getPhoneNumber(), "company");

                } else {
                    Toast.makeText(LoginActivity.this, "No company found. Please signup first", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Company> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private class userTypeOnTouch implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Button button = (Button) v;
            clearButtonState();
            button.setPressed(true);
            button.setTextColor(Color.WHITE);
            return true;
        }

        private void clearButtonState() {
            user.setPressed(false);
            company.setPressed(false);
            user.setTextColor(getResources().getColor(R.color.colorPrimary));
            company.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}


