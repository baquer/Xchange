package com.example.xchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class uploadProduct extends AppCompatActivity {

    private ImageView selectImage;
    private EditText productName, description;
    private RadioButton radioBt1, radioBt2;
    private Button submitButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Product pdt;

    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        selectImage = findViewById(R.id.imageView);
        productName = (EditText) findViewById(R.id.product_name);
        description = (EditText) findViewById(R.id.description);
        radioBt1 = (RadioButton) findViewById(R.id.radio_one);
        radioBt2 = (RadioButton) findViewById(R.id.radio_two);
        submitButton = (Button) findViewById(R.id.submit);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemImage();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });
    }


    private void uploadData() {
        String product = productName.getText().toString();
        String desc = description.getText().toString();
        String type = "donate";


        Product prod = new Product(product,desc,type);
        FirebaseDatabase.getInstance().getReference("ProductDetails").push().setValue(prod).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(uploadProduct.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(uploadProduct.this, "Data Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void selectItemImage(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == 1
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            selectImage.setVisibility(View.VISIBLE);
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                selectImage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}