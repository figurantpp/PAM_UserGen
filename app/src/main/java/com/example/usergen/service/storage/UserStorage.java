package com.example.usergen.service.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.usergen.model.User;
import com.example.usergen.service.http.OnlineImageResource;
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

        user.getProfileImage().getBitmapSync().compress(Bitmap.CompressFormat.PNG, 100, output);

        return output.toByteArray();
    }


    public void storeModel(@NonNull User user) throws IOException {

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
        statement.bindLong(7, user.getAge());
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

        return new User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                ApiDate.dateFromString(cursor.getString(5)),
                cursor.getShort(6),
                cursor.getString(8),
                new OnlineImageResource(getBlobBitmap(cursor.getBlob(7)))
        );
    }

    @NonNull
    public List<User> listStoredUsers() {

        ArrayList<User> users;

        SQLiteDatabase database = getDatabase();

        Cursor cursor = database.rawQuery(
                "select id, title, name, email, gender, birthDate, age, profileImage, nationality from UserModel;",
                null);

        users = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
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
