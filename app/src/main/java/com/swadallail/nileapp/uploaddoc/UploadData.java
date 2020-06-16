package com.swadallail.nileapp.uploaddoc;

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

import com.swadallail.nileapp.R;
import com.swadallail.nileapp.data.MainResponse;
import com.swadallail.nileapp.data.OrderBody;
import com.swadallail.nileapp.data.OrderResponse;
import com.swadallail.nileapp.data.RequestRepreBody;
import com.swadallail.nileapp.databinding.ActivityUploadDataBinding;
import com.swadallail.nileapp.helpers.SharedHelper;
import com.swadallail.nileapp.network.ApiInterface;
import com.swadallail.nileapp.orderpage.MakeOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class UploadData extends AppCompatActivity {
    ActivityUploadDataBinding binding;
    MyClick handlers;
    ProgressDialog dialog;
    int PERMISSION_REQUEST_CODE = 200;
    Bitmap user_natface, user_natback, user_profile, user_lic;
    String encodedImagenatFace, encodednatBackground, encodedprofile, encodedlic, idNum, phone, fullname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_data);
        handlers = new MyClick(this);
        binding.setHandlers(handlers);
    }

    public class MyClick {
        Context con;

        public MyClick(Context con) {
            this.con = con;
        }

        public void sendData(View view) {
            hitRequestApi();
        }

        public void natFace(View view) {
            final int GALLERY = 1;
            final int CAM = 2;
            showPictureDialognatface(GALLERY, CAM);
        }

        public void natBackground(View view) {
            final int GALLERY = 3;
            final int CAM = 4;
            showPictureDialognatface(GALLERY, CAM);
        }

        public void profile(View view) {
            final int GALLERY = 5;
            final int CAM = 6;
            showPictureDialognatface(GALLERY, CAM);
        }

        public void lic(View view) {
            final int GALLERY = 7;
            final int CAM = 8;
            showPictureDialognatface(GALLERY, CAM);
        }

    }

    private void hitRequestApi() {
        ByteArrayOutputStream profile = new ByteArrayOutputStream();
        if (user_profile != null) {
            user_profile.compress(Bitmap.CompressFormat.JPEG, 50, profile);
            encodedprofile = Base64.encodeToString(profile.toByteArray(), Base64.DEFAULT);
        } else {
            encodedprofile = null;
        }
        ByteArrayOutputStream lic = new ByteArrayOutputStream();
        if (user_lic != null) {
            user_lic.compress(Bitmap.CompressFormat.JPEG, 50, lic);
            encodedlic = Base64.encodeToString(lic.toByteArray(), Base64.DEFAULT);
        } else {
            encodedlic = null;
        }
        ByteArrayOutputStream natback = new ByteArrayOutputStream();
        if (user_natback != null) {
            user_natback.compress(Bitmap.CompressFormat.JPEG, 50, natback);
            encodednatBackground = Base64.encodeToString(natback.toByteArray(), Base64.DEFAULT);
        } else {
            encodednatBackground = null;
        }
        ByteArrayOutputStream natface = new ByteArrayOutputStream();
        if (user_natface != null) {
            user_natface.compress(Bitmap.CompressFormat.JPEG, 50, natface);
            encodedImagenatFace = Base64.encodeToString(natface.toByteArray(), Base64.DEFAULT);
        } else {
            encodedImagenatFace = null;
        }
        fullname = binding.edName.getText().toString();
        idNum = binding.edNatid.getText().toString();
        phone = binding.edPhone.getText().toString();
        if (fullname.isEmpty()) {
            binding.edName.setError("ادخل الاسم");
        } else if (idNum.isEmpty()) {
            binding.edNatid.setError("ادخل رقم الهوية");
        } else if (phone.isEmpty()) {
            binding.edPhone.setError("ادخل رقم الهاتف الخاص بك");
        }else if (encodedImagenatFace.isEmpty()) {
            Toast.makeText(UploadData.this, "رجاء قم بأدخال صورة وجه البطاقة", Toast.LENGTH_SHORT).show();
        }
        else if (encodednatBackground.isEmpty()) {
            Toast.makeText(UploadData.this, "رجاء قم بأدخال صورة ظهر البطاقة", Toast.LENGTH_SHORT).show();
        }
        else if (encodedprofile.isEmpty()) {
            Toast.makeText(UploadData.this, "رجاء قم بأدخال الصورة الشخصية", Toast.LENGTH_SHORT).show();
        }
        else {
            dialog = new ProgressDialog(UploadData.this);
            dialog.setMessage(getApplicationContext().getResources().getString(R.string.the_data_is_loaded));
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://test.nileappco.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiInterface userclient = retrofit.create(ApiInterface.class);
            String to = "Bearer " + SharedHelper.getKey(UploadData.this, "token");
            RequestRepreBody body = new RequestRepreBody(fullname, phone, encodedImagenatFace, encodednatBackground, encodedlic, encodedprofile);
            Call<MainResponse> call = userclient.requestRepre(to, body);
            call.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Toast.makeText(UploadData.this, "تم ارسال طلبكم بنجاح", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(UploadData.this, "هناك خطأ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(UploadData.this, "هناك خطأ الرجاء المحاولة مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void requestPermissiongallary() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    private void requestPermissioncamera() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

    }

    private void showPictureDialognatface(int gallery, int Cam) {
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
                                    choosePhotoFromGallary(gallery);
                                } else {
                                    requestPermissiongallary();
                                }

                                break;
                            case 1:
                                if (checkPermissioncamera()) {
                                    takePhotoFromCamera(Cam);
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

    public void choosePhotoFromGallary(int GALLERY) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera(int cam) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, cam);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case 1:
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        binding.imgIdface.setImageBitmap(bitmap);
                        user_natface = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(UploadData.this, "خطأ فى رفع الصورة!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if (data != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    binding.imgIdface.setImageBitmap(thumbnail);
                    user_natface = thumbnail;
                    Toast.makeText(UploadData.this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        binding.imgIdback.setImageBitmap(bitmap);
                        user_natback = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(UploadData.this, "خطأ فى رفع الصورة!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 4:
                if (data != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    binding.imgIdback.setImageBitmap(thumbnail);
                    user_natback = thumbnail;
                    Toast.makeText(UploadData.this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        binding.imgProfile.setImageBitmap(bitmap);
                        user_profile = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(UploadData.this, "خطأ فى رفع الصورة!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 6:
                if (data != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    binding.imgProfile.setImageBitmap(thumbnail);
                    user_profile = thumbnail;
                    Toast.makeText(UploadData.this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        binding.imgLic.setImageBitmap(bitmap);
                        user_lic = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(UploadData.this, "خطأ فى رفع الصورة!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 8:
                if (data != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    binding.imgLic.setImageBitmap(thumbnail);
                    user_lic = thumbnail;
                    Toast.makeText(UploadData.this, "تم اختيار الصورة", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }
}
