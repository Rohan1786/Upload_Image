package com.example.uploadimage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uploadimage.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
Button btn1,btn2;
Uri imageURI;
StorageReference storageReference;
ImageView imageView;

ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=findViewById(R.id.select);
        btn2=findViewById(R.id.upload);
        imageView=findViewById(R.id.imageView);


        btn1.setOnClickListener(view -> selectImage());
        btn2.setOnClickListener(view -> uploadImage());

    }
private void uploadImage(){

progressDialog=new ProgressDialog(this);
progressDialog.setTitle("uploading File....");
progressDialog.show();


        SimpleDateFormat formatter=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now=new Date();
        String filename=formatter.format(now);

storageReference= FirebaseStorage.getInstance().getReference("images/"+filename);

storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
binding.imageView.setImageURI(null);

        Toast.makeText(MainActivity.this, "Successsfully uploaded", Toast.LENGTH_SHORT).show();

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(MainActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
});

}
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100 );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==100 && data !=null && data.getData() !=null){
            imageURI=data.getData();
            binding.imageView.setImageURI(imageURI);

        }
    }
}
