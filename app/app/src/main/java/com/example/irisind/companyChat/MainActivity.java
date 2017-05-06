package com.example.irisind.companyChat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.irisind.companyChat.Adapters.CompanyListAdapter;
import com.example.irisind.companyChat.Controllers.Application;
import com.example.irisind.companyChat.Controllers.Session;
import com.example.irisind.companyChat.Models.Company;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Application app;

    private RecyclerView companyListRecyclerView;
    private CompanyListAdapter companyListAdapter;
    private List<Company> companyList = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MainActivity","created");

        app = (Application) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        companyListRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        companyListRecyclerView.setLayoutManager(layoutManager);

        companyListAdapter = new CompanyListAdapter(this,companyList);
        companyListRecyclerView.setAdapter(companyListAdapter);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            Session.getInstance().logout(MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        if (Session.getInstance().getUser() == null && Session.getInstance().getCompany() == null){
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            Log.e("MainActivity","Session = "+Session.getInstance().getUserType());
            app = (Application) getApplication();
            app.getApi().getCompanyList().enqueue(new Callback<List<Company>>() {
                @Override
                public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                    if (response.code() == 200){
                        companyList = response.body();
                        companyListAdapter.changeDataset(companyList);
                    }
                }

                @Override
                public void onFailure(Call<List<Company>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }


    }

    @Subscribe
    public void onEvent(Session.LoginStatus loginStatus){
        Log.e("MainActivity", loginStatus.name());
        if (loginStatus == Session.LoginStatus.NOT_LOGIN || loginStatus == Session.LoginStatus.LOGIN_IN_PROGRESS) {

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

    }
}
