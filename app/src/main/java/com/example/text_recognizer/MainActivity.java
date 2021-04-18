package com.example.text_recognizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE=1;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    ImageView imageView;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.image_view);
        tv =(TextView)findViewById(R.id.tv);

        // Ask User For Camera Permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 5);
        }
    }

    //Open Gallery
    public void open_gallery(View view) {
        tv.setText("");
        Intent gg = new Intent();
        gg.setType("image/*");
        gg.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gg.createChooser(gg,"select"),PICK_IMAGE);
    }

    //Show Selected Image on Image View
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public void detect(View view) {
            try {
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational())
                {
                    Toast.makeText(getApplicationContext(),"Erorrrr",Toast.LENGTH_SHORT).show();
                }else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i=0;i<items.size();i++)
                    {
                        TextBlock myitem = items.valueAt(i);
                        sb.append(myitem.getValue());
                        sb.append("\n");
                    }
                    tv.setText(sb.toString());
                    if (tv.length() == 0){
                        Toast.makeText(getApplicationContext(), "Sorry Can't Detect Text", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please Select Image First", Toast.LENGTH_SHORT).show();
            }
    }// Detect Void End

    public void instance(View view) {
        Intent intent = new Intent(this,Main_instance.class);
        startActivity(intent);
    } //Instance Void End


    public void clear(View view) {
        tv.setText("Let's Detect Some Text :)");
        imageView.setImageDrawable(null);

    }
}//Class End




    // Copy To Clip Board Method
//    void copy (){
//        String text = tv.getText().toString();
//        myClip = ClipData.newPlainText("text", text);
//        myClipboard.setPrimaryClip(myClip);
//        Toast.makeText(getApplicationContext(), "Text Copied",
//                Toast.LENGTH_SHORT).show();
//    }
