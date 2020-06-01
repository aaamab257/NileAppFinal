package com.swadallail.nileapp.Drawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swadallail.nileapp.R;

public class ReportProblem extends AppCompatActivity {
    EditText email, subject, problem;
    String emailTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //email = (EditText) findViewById(R.id.txtTo);
       // email.setText("swad415@gmail.com");
        emailTo = "info@nileappco.com";
        subject = (EditText) findViewById(R.id.txtSub);
        problem = (EditText) findViewById(R.id.txtMsg);

      Button  btn = (Button)findViewById(R.id.btnSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent it = new Intent(Intent.ACTION_SEND);
                    it.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                    it.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
                    it.putExtra(Intent.EXTRA_TEXT, problem.getText());
                    it.setType("message/rfc822");
                    startActivity(Intent.createChooser(it, "Choose Mail App"));
                }
                catch (Exception e)
                {
                    Toast.makeText(ReportProblem.this,
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
