package com.example.usergen.model.user;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.usergen.model.OnlineImageResource;
import com.example.usergen.util.ApiDate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    @NonNull
    private final Context context;

    public static final String DATABASE_NAME = "usergen_db_01";

    public UserStorage(@NonNull Context context) {

        this.context = context;

        SQLiteDatabase database = getDatabase();
        database.releaseReference();
    }

    @NonNull
    private SQLiteDatabase getDatabase() {

        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        database.execSQL(
                "create table if not exists UserModel (" +
                        "id Text," +
                        "title Text," +
                        "name Text," +
                        "email Text," +
                        "gender Text," +
                        "birthDate Text," +
                        "age int," +
                        "profileImage blob," +
                        "nationality text" +
                        ");"
        );


        return database;
    }

    @NonNull
    private byte[] getUserImageBlob(@NonNull User user) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        user.getProfileImage().getBitmap().compress(Bitmap.CompressFormat.PNG, 100, output);

        return output.toByteArray();
    }


    public void storeModel(@NonNull User user) throws IOException{

        SQLiteDatabase database = getDatabase();

        SQLiteStatement statement = database.compileStatement(
                "" +
                        "insert into UserModel values (?, ?, ?, ?, ?, ?, ? , ?, ?);"
        );

        statement.bindString(1, user.getId());
        statement.bindString(2, user.getTitle());
        statement.bindString(3, user.getName());
        statement.bindString(4, user.getEmail());
        statement.bindString(5, user.getGender());
        statement.bindString(6, ApiDate.formatDate(user.getBirthDate()));
        statement.bindLong(7, (long) user.getAge());
        statement.bindBlob(8, getUserImageBlob(user));
        statement.bindString(9, user.getNationality());

        statement.execute();
    }

    @NonNull
    private Bitmap getBlobBitmap(@NonNull byte[] bytes) {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }

    @NonNull
    private User getUserFromCursor(@NonNull Cursor cursor) {

        User user = new User();

        user.setId(cursor.getString(0));
        user.setTitle(cursor.getString(1));
        user.setName(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setGender(cursor.getString(4));
        user.setBirthDate(ApiDate.dateFromString(cursor.getString(5)));
        user.setAge(cursor.getShort(6));

        byte[] bytes = cursor.getBlob(7);

        user.setProfileImage(new OnlineImageResource(getBlobBitmap(bytes)));

        user.setNationality(cursor.getString(8));

        return user;
    }

    @NonNull
    public List<User> listStoredUsers() {

        ArrayList<User> users;

        SQLiteDatabase database = getDatabase();

        Cursor cursor = database.rawQuery(
                "select id, title, name, email, gender, birthDate, age, profileImage, nationality from UserModel;",
                null);

        users = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext())
        {
            users.add(getUserFromCursor(cursor));
        }

        cursor.close();

        return users;
    }

    public void clear() {

        SQLiteDatabase database = getDatabase();

        database.execSQL("delete from UserModel;");

    }

}
