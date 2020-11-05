package com.example.winko;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.winko.DB.DB;
import com.example.winko.DB.models.WineModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewFragment extends Fragment {
    Context ctx;
    Bitmap thumbnail;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        ctx = container.getContext();
        return inflater.inflate(R.layout.fragment_new, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View refView = view;

        view.findViewById(R.id.form_photo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
            }
        });

        view.findViewById(R.id.form_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thumbnail == null){
                    CharSequence text = "Dodaj zdjęcie!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(ctx, text, duration);
                    toast.show();
                    return;
                }
                //name
                EditText et = (EditText) refView.findViewById(R.id.form_nameField);
                String name = et.getText().toString();

                //rating
                RatingBar rb = (RatingBar) refView.findViewById(R.id.form_ratingBar);
                float rating = rb.getRating();

                //image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap scaled = scale(thumbnail,720,900);
                scaled.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bArray = bos.toByteArray();

                DB db = new DB(ctx);
                WineModel w = new WineModel(name, rating, bArray);
                if(db.insert(w)){
                    CharSequence text = "Dodano !";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(ctx, text, duration);
                    toast.show();
                    NavHostFragment.findNavController(NewFragment.this)
                            .navigate(R.id.action_NewFragment_to_MainFragment);
                }else{
                    CharSequence text = "Coś poszło nie tak ...";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(ctx, text, duration);
                    toast.show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Uri selectedImageUri = data.getData();
        try {
            InputStream ims = ctx.getContentResolver().openInputStream(selectedImageUri);
            thumbnail = BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // Scale a bitmap preserving the aspect ratio.
    private Bitmap scale(Bitmap bitmap, int maxWidth, int maxHeight) {
        // Determine the constrained dimension, which determines both dimensions.
        int width;
        int height;
        float widthRatio = (float)bitmap.getWidth() / maxWidth;
        float heightRatio = (float)bitmap.getHeight() / maxHeight;
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = maxWidth;
            height = (int)(((float)width / bitmap.getWidth()) * bitmap.getHeight());
        }
        // Height constrained.
        else {
            height = maxHeight;
            width = (int)(((float)height / bitmap.getHeight()) * bitmap.getWidth());
        }
        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float ratioX = (float)width / bitmap.getWidth();
        float ratioY = (float)height / bitmap.getHeight();
        float middleX = width / 2.0f;
        float middleY = height / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }
}