package com.example.winko;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.winko.DB.DB;
import com.example.winko.DB.models.WineModel;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<WineModel> {
      private Context ctx;
      private ArrayList<WineModel> wines;
      ArrayAdapter<WineModel> holder;

    public ListAdapter(Context context, ArrayList<WineModel> wines) {
        super(context, 0, wines);
        ctx = context;
        this.wines = wines;
        holder = this;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final WineModel wine = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.wine_list_name);
        ImageView img = (ImageView) convertView.findViewById(R.id.list_img);
        RatingBar rating = (RatingBar) convertView.findViewById(R.id.rating);
        ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.btn_delete);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB db = new DB(ctx);
                if(db.delete(wine.getID()) == 1){
                    wines.remove(position);
                    holder.notifyDataSetChanged();
                }else{
                    CharSequence text = "Coś poszło nie tak ...";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(ctx, text, duration);
                    toast.show();
                }
            }
        });

        byte[] imgByteArr = wine.getPhotoAsByteArray();
        if(imgByteArr != null){
            Bitmap bMap = BitmapFactory.decodeByteArray(imgByteArr, 0, imgByteArr.length);
            img.setImageBitmap(bMap);
        }

        name.setText(wine.getName());
        rating.setRating(wine.getRating());

        return convertView;
    }

}
