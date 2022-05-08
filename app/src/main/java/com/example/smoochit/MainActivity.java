package com.example.smoochit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {

    private ImageView capturedImageView;
    private ImageView smoochItImageView;

    private Switch option1Switch;
    private Switch option2Switch;
    private Switch option3Switch;
    private Switch option4Switch;

    Toast toastOption1;
    Toast toastOption2;
    Toast toastOption3;
    Toast toastOption4;

    String action;
    String type;

    Intent intent;
    Uri imageUri;

    private static final int SELECT_IMAGE = 100;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {

        capturedImageView = (ImageView) findViewById(R.id.capturedImageView);
        smoochItImageView = (ImageView) findViewById(R.id.smoochItImageView);

        TextView option1TextView = (TextView) findViewById(R.id.option1TextView);
        TextView option2TextView = (TextView) findViewById(R.id.option2TextView);
        TextView option3TextView = (TextView) findViewById(R.id.option3TextView);
        TextView option4TextView = (TextView) findViewById(R.id.option4TextView);

        option1Switch = (Switch) findViewById(R.id.option1Switch);
        option2Switch = (Switch) findViewById(R.id.option2Switch);
        option3Switch = (Switch) findViewById(R.id.option3Switch);
        option4Switch = (Switch) findViewById(R.id.option4Switch);

        toastOption1 = Toast.makeText(getApplicationContext(), "image sent to " + option1TextView.getText(), Toast.LENGTH_SHORT);
        toastOption2 = Toast.makeText(getApplicationContext(), "image sent to " + option2TextView.getText(), Toast.LENGTH_SHORT);
        toastOption3 = Toast.makeText(getApplicationContext(), "image sent to " + option3TextView.getText(), Toast.LENGTH_SHORT);
        toastOption4 = Toast.makeText(getApplicationContext(), "image sent to " + option4TextView.getText(), Toast.LENGTH_SHORT);

        option1Switch.setChecked(false);
        option2Switch.setChecked(false);
        option3Switch.setChecked(false);
        option4Switch.setChecked(false);

        capturedImageView.setOnClickListener(v -> {
            addImageFromGallery();
        });

        smoochItImageView.setOnClickListener(v -> {
            if (option1Switch.isChecked() || option2Switch.isChecked() || option3Switch.isChecked() || option4Switch.isChecked()) {
                sendEmailPre(option1Switch.isChecked(), option2Switch.isChecked(), option3Switch.isChecked(), option4Switch.isChecked());
            }
        });

        disableViews();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            if (type.startsWith("image/")) {

                imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

                if (imageUri != null) {
                    capturedImageView.setImageURI(imageUri);
                    enableViews();
                }
            }
        }
    }

    private void disableViews() {
        option1Switch.setEnabled(false);
        option2Switch.setEnabled(false);
        option3Switch.setEnabled(false);
        option4Switch.setEnabled(false);

        smoochItImageView.setEnabled(false);
    }

    private void enableViews() {
        option1Switch.setEnabled(true);
//        option2Switch.setEnabled(true);
//        option3Switch.setEnabled(true);
//        option4Switch.setEnabled(true);

        smoochItImageView.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendEmailPre(boolean option1Checked, boolean option2Checked, boolean option3Checked, boolean option4Checked) {

        Thread thread = new Thread(this::sendEmailPost);
        thread.start();

        if (option1Checked) {
            toastOption1.show();
        }

        if (option2Checked) {
            toastOption2.show();
        }

        if (option3Checked) {
            toastOption3.show();
        }

        if (option4Checked) {
            toastOption4.show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void sendEmailPost() {

        PostImageRequest postImageRequest = null;
        try {
            postImageRequest = new PostImageRequest
                    (encodeImageBase64(this.getIntent()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        assert postImageRequest != null;
        postImageRequest.postCall();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encodeImageBase64(Intent intent) {

        String encodedBase64Image = null;

        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap
                    (getContentResolver(), imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] bytes=byteArrayOutputStream.toByteArray();

            encodedBase64Image = Base64.getEncoder().encodeToString(bytes);

        } catch (IOException e) {

            e.printStackTrace();
        }

        return encodedBase64Image;
    }

    private void addImageFromGallery() {
        disableViews();
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intentData) {

        super.onActivityResult(requestCode, resultCode, intentData);
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE) {
            imageUri = intentData.getData();
            capturedImageView.setImageURI(imageUri);
            enableViews();
        }
    }

}
