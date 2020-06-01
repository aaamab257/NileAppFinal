package com.swadallail.nileapp;



    import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.appcompat.widget.Toolbar;


public class LogInActivity extends AppCompatActivity {

        Toolbar toolbar;
        EditText email,password;
        CheckBox checkBox;
        ImageButton signin;
        Button signup;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);



           // toolbar = (Toolbar) findViewById(R.id.toolbar);
            //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

           /*

            email = (EditText)findViewById(R.id.email);
            password = (EditText)findViewById(R.id.password);

            checkBox = (CheckBox)findViewById(R.id.checkbox);

            signin =(ImageButton)findViewById(R.id.signin);


            signin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(LogInActivity.this,"Sign In Button Clicked",Toast.LENGTH_LONG).show();
                }
            });

*/
            signup = (Button) findViewById(R.id.signup);
            signup.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LogInActivity.this,SignUpActivity.class);
                    startActivity(i);
                }
            });
        }
    }