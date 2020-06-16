package com.swadallail.nileapp.orderpage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.swadallail.nileapp.MainActivity;
import com.swadallail.nileapp.Map.MapActivty;
import com.swadallail.nileapp.R;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.OrderBody;
import com.swadallail.nileapp.data.OrderResponse;
import com.swadallail.nileapp.data.UserDataResponse;
import com.swadallail.nileapp.data.UserResponse;
import com.swadallail.nileapp.databinding.ActivityMakeOrderBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.loginauth.LoginAuthActivity;
import com.swadallail.nileapp.network.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MakeOrder extends AppCompatActivity {
    ActivityMakeOrderBinding binding;
    int PERMISSION_REQUEST_CODE = 200;
    private final int GALLERY = 2;
    MyClick handlers;
    Bitmap user_imgn;
    ProgressDialog dialog;
    int LAUNCH_MAP_FROM = 4;
    int LAUNCH_MAP_TO = 3;
    double latFrom = 0.0 , latTo = 0.0, lngFrom= 0.0 , lngTo= 0.0;
    String encodedImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_make_order);
        handlers = new MyClick(this);
        binding.setHandlers(handlers);
    }

    public class MyClick {
        Context context;

        public MyClick(Context context) {
            context = this.context;
        }

        public void chooseOrderImg(View view) {
            showPictureDialog();
        }

        public void loadData(View view) {
            loadOrder();
        }

        public void plus(View view) {
            int i = Integer.parseInt(binding.txtTime.getText().toString());
            int f = i + 1;
            if(f > 6){
                Toast.makeText(MakeOrder.this, "الحد الأقصى لعدد الساعات 6 ساعات", Toast.LENGTH_SHORT).show();
            }else {
                binding.txtTime.setText(String.valueOf(f));
            }

        }

        public void min(View view) {
            int g = Integer.parseInt(binding.txtTime.getText().toString());
            if (g == 1) {
                Toast.makeText(MakeOrder.this, "اقل وقت خلال ساعة", Toast.LENGTH_SHORT).show();
            } else {
                int c = g - 1;
                binding.txtTime.setText(String.valueOf(c));
            }
        }

        public void from(View view) {
            Intent i = new Intent(MakeOrder.this, MapActivty.class);
            startActivityForResult(i, LAUNCH_MAP_FROM);
        }

        public void to(View view) {
            Intent i = new Intent(MakeOrder.this, MapActivty.class);
            startActivityForResult(i, LAUNCH_MAP_TO);
        }
    }



    private void requestPermissiongallary() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    private void requestPermissioncamera() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("اختر الصورة من");
        String[] pictureDialogItems = {
                "معرض الصور",
                "الكاميرا"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (checkPermissiongallary()) {
                                    choosePhotoFromGallary();
                                } else {
                                    requestPermissiongallary();
                                }

                                break;
                            case 1:
                                if (checkPermissioncamera()) {
                                    takePhotoFromCamera();
                                } else {
                                    requestPermissioncamera();
                                }

                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private boolean checkPermissiongallary() {
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissioncamera() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == LAUNCH_MAP_FROM) {
            if (resultCode == RESULT_OK) {

                latFrom = data.getDoubleExtra("lat",0);
                lngFrom = data.getDoubleExtra("lng",0);
                Log.e("LatFrom","" +latFrom);
                Log.e("LngFrom",""+lngFrom);
            }
        } else if (requestCode == LAUNCH_MAP_TO) {
            if (resultCode == RESULT_OK) {

                latTo = data.getDoubleExtra("lat",0);
                lngTo = data.getDoubleExtra("lng",0);
                Log.e("Latto","" +latTo);
                Log.e("Lngto",""+lngTo);
            }
        } else if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    binding.imgOrder.setImageBitmap(bitmap);
                    user_imgn = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MakeOrder.this, "خطأ فى رفع الصورة!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            binding.imgOrder.setImageBitmap(thumbnail);
            user_imgn = thumbnail;
            Toast.makeText(MakeOrder.this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
        }

    }
    private void loadOrder() {

        String image = "";
        String order = binding.edOrdertext.getText().toString();
        int time = Integer.parseInt(binding.txtTime.getText().toString());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (user_imgn != null) {
            user_imgn.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        } else {
            encodedImage = null ;
        }

        String fromAddress = binding.edLocationfrom.getText().toString();
        String tpAddress = binding.edOrderlocationto.getText().toString();
        if(tpAddress.isEmpty()){
            Toast.makeText(this, "قم بأدخال عنوان الاستلام", Toast.LENGTH_SHORT).show();
        }else if (order.isEmpty()){
            Toast.makeText(this, "قم بأدخال الطلب", Toast.LENGTH_SHORT).show();
        }
        else {
            dialog = new ProgressDialog(MakeOrder.this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://test.nileappco.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface userclient = retrofit.create(ApiInterface.class);
            String to = "Bearer "+SharedHelper.getKey(MakeOrder.this , "token");
            OrderBody body = new OrderBody(encodedImage,order,String.valueOf(latFrom),String.valueOf(lngFrom),String.valueOf(latTo),String.valueOf(lngTo),time,fromAddress,tpAddress);
            Call<MainResponse<OrderResponse>> call = userclient.UploadOrder(to ,body);
            call.enqueue(new Callback<MainResponse<OrderResponse>>() {
                @Override
                public void onResponse(Call<MainResponse<OrderResponse>> call, Response<MainResponse<OrderResponse>> response) {
                    dialog.dismiss();
                    Log.e("Done",""+response.body());
                    if(response.body() != null){
                        Toast.makeText(MakeOrder.this, "تم ارسال الطلب بنجاح الرجاء انتظار الرد", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MakeOrder.this , MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse<OrderResponse>> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(MakeOrder.this, "هناك خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
