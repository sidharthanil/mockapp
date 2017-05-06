package com.example.irisind.companyChat;

import android.app.FragmentTransaction;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.irisind.companyChat.Controllers.Application;
import com.example.irisind.companyChat.Dialogs.DateDialog;
import com.example.irisind.companyChat.Models.User;
import com.example.irisind.companyChat.Utilities.ISO8601;
import com.example.irisind.companyChat.Utilities.PermissionCheck;
import com.example.irisind.companyChat.Utilities.SetData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserProfileActivity extends AppCompatActivity implements SetData {

    private static final String TAG = "EditUserProfile";
    private static final int SELECT_IMGAE = 100;
    private Application app;
    private EditText fullname, age, place, city, phoneNumber;
    private TextView dob;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private String isodate;
    private CircleImageView profPic;
    private String profile_image = null;
    private Button signup;
    private String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        app = (Application) getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("TITLE"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullname = (EditText) findViewById(R.id.input_name);
        age = (EditText) findViewById(R.id.input_age);
        dob = (TextView) findViewById(R.id.input_dob);
        place = (EditText) findViewById(R.id.input_place);
        city = (EditText) findViewById(R.id.input_city);
        phoneNumber = (EditText) findViewById(R.id.input_phone);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSexGroup);
        profPic = (CircleImageView) findViewById(R.id.profile_image);
        signup = (Button) findViewById(R.id.signup);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog dialog = new DateDialog();
                dialog.setKey("DOB");
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                dialog.show(fragmentTransaction, "DatePicker");
            }
        });

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionCheck.isStorageAccessAvailable(EditUserProfileActivity.this)) {
                    PermissionCheck.requestStoragePermission(EditUserProfileActivity.this);
                    return;
                } else {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Image"),
                            SELECT_IMGAE);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidDetails()) {
                    app.getApi().signupUser(fullname.getText().toString(), age.getText().toString(), sex, isodate, phoneNumber.getText().toString(),
                            place.getText().toString(), city.getText().toString(), "http://www.hbc333.com/data/out/190/47163361-profile-pictures.png")
                            .enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(EditUserProfileActivity.this, "Signedup successfully as user.Login to continue", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditUserProfileActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                }


            }
        });
    }

    @Override
    public void setData(String key, Object val) {
        Calendar cal;
        SimpleDateFormat sdf_date, sdf_time;
        sdf_date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        sdf_time = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        if (key == null) return;
        if (key.equals("DOB")) {
            cal = (Calendar) val;
            dob.setText(sdf_date.format(cal.getTime()));
            isodate = ISO8601.fromCalendar(cal);


        }


    }

    public boolean isValidDetails() {

        int selectedid = 0;

        if (fullname.getText().toString().replaceAll(" ", "").length() == 0) {
            Toast.makeText(EditUserProfileActivity.this, "Check your name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (age.getText().toString().replaceAll(" ", "").length() == 0) {
            if (Integer.parseInt(age.getText().toString()) <= 0) {
                Toast.makeText(EditUserProfileActivity.this, "Check your name", Toast.LENGTH_SHORT).show();
                return false;
            }

        }

//        if (profile_image == null){
//            Toast.makeText(EditUserProfileActivity.this, "Add a profile picture", Toast.LENGTH_SHORT).show();
//        }
        if (dob.getText().toString().replaceAll(" ", "").length() == 0) {
            Toast.makeText(EditUserProfileActivity.this, "Check your dob", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (place.getText().toString().replaceAll(" ", "").length() == 0) {
            Toast.makeText(EditUserProfileActivity.this, "Check your place", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (city.getText().toString().replaceAll(" ", "").length() == 0) {
            Toast.makeText(EditUserProfileActivity.this, "Check your city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phoneNumber.getText().length() < 6) {
            Toast.makeText(EditUserProfileActivity.this, "Enter correct phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (Long.parseLong(phoneNumber.getText().toString())>0) {
//            Toast.makeText(EditUserProfileActivity.this, "Enter correct phone number", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        selectedid = radioSexGroup.getCheckedRadioButtonId();

        if (selectedid == 0) {
            Toast.makeText(EditUserProfileActivity.this, "Select Male/Female", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            radioSexButton = (RadioButton) findViewById(selectedid);
            sex = radioSexButton.getText().toString();

        }


        return true;
    }

    private void loadProfPic(String profilePicture) {
        Glide.with(EditUserProfileActivity.this).load(profilePicture)
                .asBitmap().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profPic);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMGAE && resultCode == RESULT_OK
                && null != data) {

            try {


                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                String selectedImagePath = null;

                CursorLoader cursorLoader = new CursorLoader(EditUserProfileActivity.this, selectedImage, filePathColumn, null, null,
                        null);
                // Get the cursor
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    selectedImagePath = cursor.getString(column_index);
                } else {
                    selectedImagePath = null;
                }
                if (selectedImagePath == null) {
                    //2:OI FILE Manager --- call method: uri.getPath()
                    selectedImagePath = selectedImage.getPath();
                }
                profile_image = selectedImagePath;
                loadProfPic(profile_image);
                Log.e("selectedImagepath", selectedImagePath);

                // Move to first row
                /*cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                Log.e(TAG, "gallery image string = " + imgDecodableString);
                pic = imgDecodableString;
                loadProfPic(pic);
*/

                cursor.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
