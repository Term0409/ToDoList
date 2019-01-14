package com.example.sirishaa.todo;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sirishaa.todo.db.TaskDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TaskDBHelper mHelper; //private instance of class TaskDBHelper
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    //VIEW ITEMS
    TextView tv_1;
    FloatingActionButton add_bt;

    //SIDE BAR
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_bt = (FloatingActionButton) findViewById(R.id.act_add);
        add_bt.setOnClickListener(this);


        //SIDE BAR

       dl = (DrawerLayout)findViewById(R.id.activity_main);
       t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.cal:
                        Intent calen = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(calen);
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                    case R.id.mycart:
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }




            }
        });


        //GET DATE AND DAY IN TITLE BAR
        tv_1 = (TextView) findViewById(R.id.tv1);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        String date = new SimpleDateFormat("dd MMMM ", Locale.getDefault()).format(new Date());
        tv_1.setText(dayOfTheWeek+"\n"+date);


        //DATABASE
        mHelper=new TaskDBHelper(this); //initialising it here
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        updateUI();

        //DISPLAY DATA FROM DB
        SQLiteDatabase db=mHelper.getReadableDatabase();
        Cursor cur= db.query("Tasks", new String[] {"_id","title"},null,null,null,null,null);
        while(cur.moveToNext())
        {
            int idx=cur.getColumnIndex("title");
        }
        cur.close();
        db.close();
    }


   //SIDE BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.act_add)

        {

            final EditText edt = new EditText(this);

            //Alertdialog.Builder is an  inner class of AlertDialog class
            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            //set messages and title of alert box
            adb.setTitle("Add a task");
            adb.setMessage("What do you want to do today?");
            adb.setView(edt);
            //Action when you add something
            adb.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String task = String.valueOf(edt.getText());
                    //Get the text then...

                    //INSERT INTO DB
                    SQLiteDatabase db = mHelper.getWritableDatabase(); //write to DB
                    ContentValues values = new ContentValues();
                    values.put("title", task); //Put the value taken from user
                    db.insertWithOnConflict("Tasks", null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    updateUI();
                }
            });

            adb.setNegativeButton("Cancel", null);

            // Create an AlertDialog by using the create() method of builder class and show the dialog
            AlertDialog ad = adb.create();
            ad.show();


            // default: return super.onOptionsItemSelected(item);
        }

    }




//To populate the list view
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query("Tasks",
                new String[]{"_id", "title"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex("title");
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete("Tasks",
                "title" + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }


}
