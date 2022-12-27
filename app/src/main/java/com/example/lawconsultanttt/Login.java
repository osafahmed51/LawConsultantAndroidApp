package com.example.lawconsultanttt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private TextInputEditText phoneinput,passinput;
    private Button loginbutton;
    private TextView forget,admin,joinnowbtn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneinput=(TextInputEditText) findViewById(R.id.phone_text_login1);
        passinput=(TextInputEditText) findViewById(R.id.password_text1);
        loginbutton=findViewById(R.id.login_btn);
        forget=findViewById(R.id.forgetbtn);
        admin=findViewById(R.id.adminbtn);
        joinnowbtn=findViewById(R.id.joinnow);
        progressBar=findViewById(R.id.progressBar_cyclic);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=phoneinput.getText().toString();
                String pass=passinput.getText().toString();
                if(phone.equals("") && pass.equals(""))
                {
                    Toast.makeText(Login.this, "Please fill all the feilds", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    final DatabaseReference loginref=FirebaseDatabase.getInstance().getReference();
                    loginref.child("Users").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                String phoneuserinp=snapshot.child("phone").getValue().toString();
                                String passuserinp=snapshot.child("password").getValue().toString();
                                if(phoneuserinp.equals(phone) && passuserinp.equals(pass))
                                {

                                    Intent userloggedin=new Intent(Login.this, dashboard.class);
                                    userloggedin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(userloggedin);
                                    finish();

                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Login.this, "Please check your Phone and Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                               progressBar.setVisibility(View.GONE);
                                Toast.makeText(Login.this, "Account with this" +phone+"can't exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        joinnowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentjoin=new Intent(Login.this,SignUp.class);
                startActivity(intentjoin);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentbacklogin=new Intent(Intent.ACTION_MAIN);
        intentbacklogin.addCategory(Intent.CATEGORY_HOME);
        intentbacklogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentbacklogin);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences sharedPreferences=getSharedPreferences("sharedpref",MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString("phone", String.valueOf(phoneinput));
//        editor.putString("password",String.valueOf(passinput));
//        editor.apply();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sharedPreferences=getSharedPreferences("sharedpref",MODE_PRIVATE);
//        String p1=sharedPreferences.getString("phone","");
//        String p2=sharedPreferences.getString("password","");
//        phoneinput.setText(p1);
//        passinput.setText(p2);
//        Intent intentorresum=new Intent(Login.this,dashboard.class);
//        startActivity(intentorresum);
//        finish();
//    }
}