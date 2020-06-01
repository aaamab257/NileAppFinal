package com.swadallail.nileapp.delegete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.databinding.ActivityDelegeteHomeBinding;

public class DelegeteHome extends AppCompatActivity {
    ActivityDelegeteHomeBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this  , R.layout.activity_delegete_home);
    }
}
