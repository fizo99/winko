package com.example.winko.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.winko.DB.models.WineModel;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {

    public DB(@Nullable Context context){
        super(context, "wines.db", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableStatement =
                "CREATE TABLE WINE (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "NAME VARCHAR(40)," +
                        "IMAGE BLOB DEFAULT NULL," +
                        "RATING FLOAT);";
        db.execSQL(tableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("Drop table wine");
    }
    public boolean insert(WineModel wine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NAME",wine.getName());
        cv.put("RATING",wine.getRating());
        cv.put("IMAGE", wine.getPhotoAsByteArray());
        long insert = db.insert("wine",null,cv);
        db.close();
        if(insert == -1) return false;
        else return true;
    }
    public int delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = "wine";
        String whereClause = "id=?";
        String[] whereArgs = new String[] { String.valueOf(id) };

        int result =  db.delete(table, whereClause, whereArgs);
        db.close();

        return result;
    }
    public List<WineModel> showAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<WineModel> result = new ArrayList<>();

        String query = "Select * from wine";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                byte[] photo = cursor.getBlob(2);
                float rating = cursor.getFloat(3);
                result.add(new WineModel(id,name,rating,photo));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
}