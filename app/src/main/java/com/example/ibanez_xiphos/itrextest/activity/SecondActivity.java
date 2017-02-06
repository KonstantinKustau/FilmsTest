package com.example.ibanez_xiphos.itrextest.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.ibanez_xiphos.itrextest.R;
import com.example.ibanez_xiphos.itrextest.database.DBHelper;

import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.INTENT_ID;
import static com.example.ibanez_xiphos.itrextest.activity.MainActivity.TAG;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_DESCRIPTION;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_NAME;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_PREMIERE;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.TABLE_FILMS;

public class SecondActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView = (TextView) findViewById(R.id.name);
        TextView textPremiere = (TextView) findViewById(R.id.premiere);
        TextView textDescription = (TextView) findViewById(R.id.description);

        dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(TABLE_FILMS, null, "id =" + getIntent().getLongExtra(INTENT_ID, -1), null, null, null, null);
        if (c.moveToFirst()) {
            textView.setText(c.getString(c.getColumnIndex(FILMS_NAME)));
            textPremiere.setText("Премьера: " + c.getString(c.getColumnIndex(FILMS_PREMIERE)));
            textDescription.setText(c.getString(c.getColumnIndex(FILMS_DESCRIPTION)));
        } else {
            Log.d(TAG, "read: 0 rows");
        }

        c.close();
        dbHelper.close();
    }
}
