package com.livebettips.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.livebettips.R;
import com.livebettips.objects.Api;
import com.livebettips.objects.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Register extends Activity {

    Button bt_register;
    EditText et_email,et_password,et_repassword;
    TextView tv_validEmail,tv_validPassword,tv_validrePassword;
    String email,password,repassword;
    Context ctx;
    User user;
    Boolean valid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ctx = this;

        bt_register = (Button) findViewById(R.id.bt_register_register);
        et_email = (EditText) findViewById(R.id.et_register_email);
        et_password = (EditText) findViewById(R.id.et_register_password);
        et_repassword = (EditText) findViewById(R.id.et_register_repassword);
        tv_validEmail = (TextView) findViewById(R.id.tv_register_validEmail);
        tv_validPassword = (TextView) findViewById(R.id.tv_register_validPassword);
        tv_validrePassword = (TextView) findViewById(R.id.tv_register_validrePassword);
        user = new User();

        bt_register.setEnabled(false);

        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                    email = et_email.getText().toString();
                    Matcher matcher = pattern.matcher(email);
                    if(!matcher.matches()){
                        tv_validEmail.setText("Please Enter a valid Email ID");
                        valid=false;
                    }else{
                        tv_validEmail.setText("Email ID is correct");
                        valid=true;
                    }
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    password = et_password.getText().toString();
                    if (password != "") {
                        if (password.length() > 5) {
                            tv_validPassword.setText("Password is correct");
                            valid=true;
                        } else {
                            tv_validPassword.setText("Password must be greater than 5 characters");
                            valid=false;
                        }
                    }else{
                            tv_validPassword.setText("This field is required");
                            valid=false;
                    }
                }
            }
        });
         et_repassword.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                   if(password.contentEquals(s)){
                       tv_validrePassword.setText("Password Match");
                       if(valid){
                           user.setEmail(email);
                           user.setPassword(password);
                           bt_register.setEnabled(true);
                       }
                   }else{
                       tv_validrePassword.setText("Password does not match");
                       bt_register.setEnabled(false);
                   }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final ProgressDialog progressDialog = new ProgressDialog(ctx);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Registering...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    Callback callback = new Callback() {
                        @Override
                        public void success(Object o, Response response) {
                            // Read response here
                            progressDialog.dismiss();
                            Log.d("Object", o.toString());
                            Toast.makeText(ctx,"Registered Successfully.\n" +
                                                "Email has been sent for verification ",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            progressDialog.dismiss();
                            Log.d("error", retrofitError.toString());
                            // Catch error here
                        } };
                    Api.userInterface.createUser(user, callback);
                }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
