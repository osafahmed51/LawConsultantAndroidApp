package com.example.lawconsultanttt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private TextInputEditText names,phones,emails,passs;
    private Button signupbutton;
    private TextView haveancc;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        names=(TextInputEditText) findViewById(R.id.name_text1);
        phones=(TextInputEditText) findViewById(R.id.phone_text1);
        emails=(TextInputEditText) findViewById(R.id.emaill_text1);
        passs=(TextInputEditText) findViewById(R.id.pass_text1);
        signupbutton=findViewById(R.id.signup_btn);
        haveancc=findViewById(R.id.alreadyaccbtn);
        progressDialog=new ProgressDialog(this);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=names.getText().toString();
                String phone=phones.getText().toString();
                String email=emails.getText().toString();
                String password=passs.getText().toString();
                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignUp.this, "Empty Name Feild!" , Toast.LENGTH_SHORT).show();
                }
               else if(TextUtils.isEmpty(phone) )
                {
                    Toast.makeText(SignUp.this, "Empty Phone Feild", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(email) && !email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"))
                {
                    Toast.makeText(SignUp.this, "Empty Email Feild!", Toast.LENGTH_SHORT).show();
                }
                else if(!(password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$")))
                {
                    Toast.makeText(SignUp.this, "No Strong Password!" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.setTitle("Create Account");
                    progressDialog.setMessage("Please wait , we are creating your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    createAccount(name,phone,email,password);

                }
            }
        });

        haveancc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenthaveac=new Intent(SignUp.this,Login.class);
                startActivity(intenthaveac);
                finish();
            }
        });

    }

    private void createAccount(String name, String phone, String email, String password) {

        final DatabaseReference signupref=FirebaseDatabase.getInstance().getReference();
        signupref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> usermap=new HashMap<>();
                    usermap.put("name",name);
                    usermap.put("phone",phone);
                    usermap.put("email",email);
                    usermap.put("password",password);
                    signupref.child("Users").child(phone).updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Account Created Successfuly", Toast.LENGTH_SHORT).show();
                                        Intent intent_AccCreated=new Intent(SignUp.this, Login.class);
                                        startActivity(intent_AccCreated);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(SignUp.this, "Account With This Number Already Exist.",  Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intentb=new Intent(Intent.ACTION_MAIN);
//        intentb.addCategory(Intent.CATEGORY_HOME);
//        intentb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intentb);
//    }
}