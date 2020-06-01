package com.swadallail.nileapp.navpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.databinding.ActivityNavBinding;

public class NavActivity extends AppCompatActivity {
    ActivityNavBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this , R.layout.activity_nav);
    }
}
