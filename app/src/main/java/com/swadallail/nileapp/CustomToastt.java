package com.swadallail.nileapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToastt {


    int x=1;

    private static Toast toast;
    // Custom Toast Method
    public void Show_Toast(Context context, String error) {

        // Layout Inflater for inflating custom view
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflate the layout over view
        View layout = inflater.inflate(R.layout.custom_toast, null);

        // Get TextView id and set error
        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(error);


        if (toast != null) {
            toast.cancel();
        }



        toast = new Toast(context);// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        // Toast
        // gravity
        // and
        // Fill
        // Horizoontal

        toast.setDuration(Toast.LENGTH_LONG);// Set Duration
        toast.setView(layout); // Set Custom View over toast

        toast.show();// Finally show toast
    }

}
