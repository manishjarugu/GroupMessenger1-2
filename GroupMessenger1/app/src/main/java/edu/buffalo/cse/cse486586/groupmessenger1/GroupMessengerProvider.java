package edu.buffalo.cse.cse486586.groupmessenger1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

/**
 * GroupMessengerProvider is a key-value table. Once again, please note that we do not implement
 * full support for SQL as a usual ContentProvider does. We re-purpose ContentProvider's interface
 * to use it as a key-value table.
 * 
 * Please read:
 * 
 * http://developer.android.com/guide/topics/providers/content-providers.html
 * http://developer.android.com/reference/android/content/ContentProvider.html
 * 
 * before you start to get yourself familiarized with ContentProvider.
 * 
 * There are two methods you need to implement---insert() and query(). Others are optional and
 * will not be tested.
 * 
 * @author stevko
 *
 */
public class GroupMessengerProvider extends ContentProvider {

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // You do not need to implement this.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /*
         * TODO: You need to implement this method. Note that values will have two columns (a key
         * column and a value column) and one row that contains the actual (key, value) pair to be
         * inserted.
         * 
         * For actual storage, you can use any option. If you know how to use SQL, then you can use
         * SQLite. But this is not a requirement. You can use other storage options, such as the
         * internal storage option that we used in PA1. If you want to use that option, please
         * take a look at the code for PA1.
         */
        String FILENAME = values.get("key").toString();
        String string = values.get("value").toString().trim();
        FileOutputStream outputStream;
        Context context=getContext();

        try {
            outputStream =  context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            Log.i(TAG, "output stream of : "+FILENAME);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Unable to write in file "+FILENAME);
        }
        Log.v("insert", values.toString());
        return uri;
    }

    @Override
    public boolean onCreate() {
        // If you need to perform any one-time initialization task, please do it here.
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // You do not need to implement this.
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        /*
         * TODO: You need to implement this method. Note that you need to return a Cursor object
         * with the right format. If the formatting is not correct, then it is not going to work.
         *
         * If you use SQLite, whatever is returned from SQLite is a Cursor object. However, you
         * still need to be careful because the formatting might still be incorrect.
         *
         * If you use a file storage option, then it is your job to build a Cursor * object. I
         * recommend building a MatrixCursor described at:
         * http://developer.android.com/reference/android/database/MatrixCursor.html
         */

        Context context=getContext();
        FileInputStream fileInputStream;
        String value=null;

        try{
            fileInputStream=context.openFileInput(selection);
            InputStreamReader queryInputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader queryBufferedReader = new BufferedReader(queryInputStreamReader);
            value = queryBufferedReader.readLine().toString().trim();
            Log.i(TAG,"Value for the key "+selection+" is : "+value);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found error while querying");
        } catch (IOException e) {
            Log.e(TAG, "IO error while querying");
        }
        Log.i(TAG,"At query.....!!!");
        String[] keyValue={"key","value"};
        MatrixCursor queryMatrixCursor= new MatrixCursor(keyValue);
        queryMatrixCursor.newRow().add(selection).add(value);
        Log.v("query", selection+" "+value);
        return queryMatrixCursor;
    }
}
