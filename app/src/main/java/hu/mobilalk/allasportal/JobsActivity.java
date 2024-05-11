package hu.mobilalk.allasportal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class JobsActivity extends AppCompatActivity {
    private static final String LOG_TAG = JobsActivity.class.toString();
    private FirebaseUser user;
    private RecyclerView myRecycleView;
    private ArrayList<JobItem> myItemList;
    private JobItemAdapter myAdapter;
    private  int gridNumber = 1;
    private int queryLimit = 8;
    private boolean viewRow = false;
    private FirebaseFirestore myFirestore;
    private CollectionReference myItems;
    private NotificationHandler myNotificationHandler;
    private AlarmManager myAlarmManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG, "Authenticated user!");
        }else{
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        myRecycleView = findViewById(R.id.recyclerView);
        myRecycleView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        myItemList = new ArrayList<>();

        myAdapter = new JobItemAdapter(this, myItemList);
        myRecycleView.setAdapter(myAdapter);

        FirebaseFirestore myFireStore = FirebaseFirestore.getInstance();
        myItems = myFireStore.collection("Items");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getMenuInflater().inflate(R.menu.job_list_menu, toolbar.getMenu());

        myFireStore = FirebaseFirestore.getInstance();
        myItems = myFireStore.collection("Items");

        queryData();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        this.registerReceiver(powerReciever,filter);

        myNotificationHandler = new NotificationHandler(this);
        myAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        setAlarmManager();
    }

    BroadcastReceiver powerReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action == null){
                return;
            }

            switch (action){
                case Intent.ACTION_POWER_CONNECTED:
                    queryLimit = 8;
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    queryLimit = 4;
                    break;

            }

            queryData();
        }
    };

    private void queryData(){
        myItemList.clear();

        myItems.orderBy("location").limit(queryLimit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                JobItem item = document.toObject(JobItem.class);
                item.setId(document.getId());
                myItemList.add(item);
            }

            if(myItemList.size() == 0){
                initilizeData();
                queryData();
            }

            myAdapter.notifyDataSetChanged();
        });
    }


    private void initilizeData() {
        String[] itemsLists = getResources().getStringArray(R.array.jobItemTitle);
        String[] itemsDescriptions = getResources().getStringArray(R.array.jobItemDescription);
        String[] itemsLongDescriptions = getResources().getStringArray(R.array.jobItemLongDescription);
        String[] itemsLocations = getResources().getStringArray(R.array.jobItemLocation);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.jobItemImages);
        TypedArray itemsRates = getResources().obtainTypedArray(R.array.jobItemRating);

        for(int i = 0; i < itemsLists.length; i++){
            myItems.add(new JobItem(
                    "",
                    itemsLists[i],
                    itemsDescriptions[i],
                    itemsLongDescriptions[i],
                    itemsLocations[i],
                    itemsImageResource.getResourceId(i,0),
                    itemsRates.getFloat(i,0))
            );
        }
        itemsImageResource.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.job_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.logout_button) {
            Log.d(LOG_TAG, "Log out clicked");
            FirebaseAuth.getInstance().signOut();
            finish();
            return true;
        } else if (id == R.id.setting_button) {
            Log.d(LOG_TAG, "Setting button clicked");
            return true;
        } else if (id == R.id.view_selector) {
            Log.d(LOG_TAG, "Change view clicked");

            if(viewRow){
                changeSpanCount(item, R.drawable.icon_view_grid_empty, 1);
            }else{
                changeSpanCount(item, R.drawable.icon_view, 2);
            }

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "JobsActivity is pausing");

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isUserLoggedIn", true);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(powerReciever, filter);

        Log.d(LOG_TAG, "JobsActivity has resumed");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerReciever);
    }

    private void changeSpanCount(MenuItem item, int drawableId, int spanCount) {
        viewRow = !viewRow;

        item.setIcon(drawableId);
        GridLayoutManager layoutManager = (GridLayoutManager)  myRecycleView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    public void deleteItem(JobItem item) {
        DocumentReference ref = myItems.document(item._getId());
        ref.delete().addOnSuccessListener(succes -> {
            Log.d(LOG_TAG, "Item deleted succesfully:"+ item._getId());
        }).addOnFailureListener(fail -> {
            Toast.makeText(this, "Item cannot be deleted: "+item._getId(),Toast.LENGTH_SHORT).show();
        });

        queryData();
    }

    public void updateItem(JobItem item) {
        myItems.document(item._getId()).update("location","Budapest").addOnSuccessListener(succes -> {
            Log.d(LOG_TAG, "Item updated succesfully:"+ item._getId());
            myNotificationHandler.send("Job's location changed to Budapest");
        }).addOnFailureListener(
                fail ->{
                    Toast.makeText(this, "Item cannot be changed: "+item._getId(),Toast.LENGTH_SHORT).show();
        });

        queryData();
    }

    private void setAlarmManager(){
        long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_MUTABLE);

        myAlarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent
        );

    }
}