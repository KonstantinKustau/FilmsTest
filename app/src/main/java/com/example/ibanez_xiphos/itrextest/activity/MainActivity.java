package com.example.ibanez_xiphos.itrextest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ibanez_xiphos.itrextest.adapter.ClickListener;
import com.example.ibanez_xiphos.itrextest.R;
import com.example.ibanez_xiphos.itrextest.adapter.RecyclerItem;
import com.example.ibanez_xiphos.itrextest.adapter.RecyclerTouchListener;
import com.example.ibanez_xiphos.itrextest.adapter.MyAdapter;
import com.example.ibanez_xiphos.itrextest.database.DBHelper;
import com.example.ibanez_xiphos.itrextest.service.MainService;
import com.example.ibanez_xiphos.itrextest.ui.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_DESCRIPTION;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_ID;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_IMAGE;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_NAME;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_NAME_ENG;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.FILMS_PREMIERE;
import static com.example.ibanez_xiphos.itrextest.database.DBHelper.TABLE_FILMS;

public class MainActivity extends AppCompatActivity {

    public final static int STATUS_SUCCESS = 100;
    public final static int STATUS_ERROR = 200;
    public final static String BROADCAST_ACTION = "com.example.ibanez_xiphos.itrextest";
    public final static String PARAM_STATUS = "status";
    public static final String TAG = "CoOstOFF";
    public static final String INTENT_ID = "ID";

    DBHelper dbHelper;

    private EmptyRecyclerView recyclerView;
    private MyAdapter adapter;
    private SwipeRefreshLayout mSwipeLayout;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh));
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getApplicationContext().startService(new Intent(getApplicationContext(), MainService.class));
            }
        });

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(PARAM_STATUS, 0);

                switch (status) {
                    case STATUS_SUCCESS:
                        database();
                        break;
                    case STATUS_ERROR:
                        Toast.makeText(getApplicationContext(), "Ошибка соединения с сервером", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Log.i(TAG, "ERROR. PARAM_STATUS = " + status);
                        break;
                }
                mSwipeLayout.setRefreshing(false);
                getApplicationContext().stopService(new Intent(getApplicationContext(), MainService.class));
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void database() {

        List<RecyclerItem> listItems = new ArrayList<>();
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(TABLE_FILMS, null, null, null, null, null, null);
        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex(FILMS_ID);
            int imageColIndex = c.getColumnIndex(FILMS_IMAGE);
            int nameColIndex = c.getColumnIndex(FILMS_NAME);
            int nameEngColIndex = c.getColumnIndex(FILMS_NAME_ENG);
            int premiereColIndex = c.getColumnIndex(FILMS_PREMIERE);
            int descriptionColIndex = c.getColumnIndex(FILMS_DESCRIPTION);

            do {
                listItems.add(new RecyclerItem(
                        c.getLong(idColIndex),
                        c.getString(imageColIndex),
                        c.getString(nameColIndex),
                        c.getString(nameEngColIndex),
                        c.getString(premiereColIndex),
                        c.getString(descriptionColIndex))
                );
            } while (c.moveToNext());
        } else {
            Log.d(TAG, "read: 0 rows");
        }

        c.close();
        dbHelper.close();

        adapter = new MyAdapter(listItems, this);
        recyclerView = (EmptyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(null);
        recyclerView.setEmptyView(findViewById(R.id.empty_rec_view));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                doSomething(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                doSomething(position);
            }

            private void doSomething(int pos) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra(INTENT_ID, adapter.getItemId(pos));
                startActivity(intent);
            }
        }));
    }

}
