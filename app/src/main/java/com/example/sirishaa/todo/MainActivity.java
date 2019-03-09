package com.example.sirishaa.todo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sirishaa.todo.Adapters.CategoryAdapter;
import com.example.sirishaa.todo.Adapters.TaskListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    static TaskListAdapter taskListAdapter;
    public static ArrayList<Task> todoList;
    public String today, selectedCategory;
    int listPosition = 0;

    static TextView titleTv;
    static EditText titleEt;
    static AppCompatButton addTaskB;

    private static RecyclerView horizontal_rv;
    private static ArrayList<String> categoryList;
    private static CategoryAdapter categoryAdapter;
    CheckBox checkBox;



    //VIEW ITEMS
    TextView tv_1;
    TextView monthTv;
    TextView yearTv;
    static TextView dayTv, myTaskTv, newTv;
    FloatingActionButton add_bt;
    String name;
    public static FirebaseAuth auth;
    public static FirebaseUser user;

    //SIDE BAR
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        newTv = findViewById(R.id.newtv);
        Intent in = getIntent();
        //name = in.getStringExtra("Name");

        add_bt = findViewById(R.id.act_add);
        add_bt.setOnClickListener(this);
        //setTitle("TODO");
        Typeface medium_tf = Typeface.createFromAsset(getAssets(),"fonts/avenir_medium.ttf");
        Typeface heavy_tf = Typeface.createFromAsset(getAssets(),"fonts/avenir_heavy.ttf");
        //SIDE BAR

       dl = findViewById(R.id.activity_main);
       t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.cal:
                        Intent calen = new Intent(getApplicationContext(), CalendarActivity.class);
                        startActivity(calen);
                        break;
                    case R.id.categories:
                        Intent categoryIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
                        startActivity(categoryIntent);
                        break;
                    case R.id.mycart:
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        auth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    default:
                        return true;
                }

             return true;

            }
        });



        //GET DATE AND DAY IN TITLE BAR
        //tv_1 = (TextView) findViewById(R.id.tv1);
        //monthTv = findViewById(R.id.month_tv);
        //yearTv = findViewById(R.id.year_tv);

        dayTv = findViewById(R.id.day_tv);
        myTaskTv = findViewById(R.id.mytasks_tv);
        newTv.setTypeface(medium_tf);
        dayTv.setTypeface(medium_tf);
        myTaskTv.setTypeface(heavy_tf);
        //myTaskTv.setText("Hi "+name+",");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        today = simpleDateFormat.format(d);
        //tv_1.setText(date);
       // monthTv.setText(month);
       // yearTv.setText(year);


        //mHelper=new TaskDBHelper(this); //initialising it here
        todoList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.task_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        taskListAdapter = new TaskListAdapter(this);
        recyclerView.setAdapter(taskListAdapter);
        dayTv.setText(String.valueOf(todoList.size())+"tasks");
        taskListAdapter.forceNotify(todoList);


        /*
        SQLiteDatabase db=mHelper.getReadableDatabase();
        Cursor cur= db.query("Tasks", new String[] {"_id","title"},null,null,null,null,null);
        while(cur.moveToNext())
        {
            int idx=cur.getColumnIndex("title");
        }
        cur.close();
        db.close();*/
    }



    //SIDE BAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void getSelectedCategory(int listPosition)
    {
        this.listPosition = listPosition;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.act_add)

        {
            final AppCompatDialog rejectDlg = new AppCompatDialog(this);
            categoryList = new ArrayList<>();
            categoryList.add("Work");
            categoryList.add("Personal");
            categoryList.add("Shopping");

            View view = LayoutInflater.from(this).inflate(R.layout.add_task_dialog, null);
            horizontal_rv= view.findViewById(R.id.categories_rv);
            addTaskB = view.findViewById(R.id.add_task_b);
            //titleTv = view.findViewById(R.id.title_tv);
            titleEt = view.findViewById(R.id.title_et);
            horizontal_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
            categoryAdapter=new CategoryAdapter(categoryList, this);
            horizontal_rv.setAdapter(categoryAdapter);


            addTaskB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskTitle = titleEt.getText().toString();
                    selectedCategory = categoryList.get(listPosition);
                    if(selectedCategory!=null)
                    {
                        saveTasksInCategories(taskTitle,selectedCategory);
                    }
                    saveTodo(taskTitle,today,selectedCategory);
                    rejectDlg.dismiss();
                }
            });
            rejectDlg.setContentView(view);
            rejectDlg.show();
        }

    }

    public static void saveTasksInCategories(String taskTitle, String selectedCategory)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String category = database.getReference(user.getUid()).child("ToDoList").child(selectedCategory).push().getKey();


        Map<String, Object> childUpdates1 = new HashMap<>();
        childUpdates1.put(category, taskTitle);
        database.getReference(user.getUid()).child("Categories").child(selectedCategory).updateChildren(childUpdates1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                }
            }
        });

    }

    public static void saveTodo(String taskTitle, String today, String selectedCategory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference(user.getUid()).child("ToDoList").child(today).push().getKey();

        Task taskNew = new Task();
        taskNew.setTaskName(taskTitle);
        taskNew.setCategory(selectedCategory);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, taskNew.toFirebaseObject());
        database.getReference(user.getUid()).child("ToDoList").child(today).updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                }
            }
        });

        database.getReference(user.getUid()).child("ToDoList").child(today).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        todoList.clear();

                        Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Task task = data.getValue(Task.class);
                            todoList.add(task);
                        }

                        taskListAdapter.forceNotify(todoList);
                        dayTv.setText(String.valueOf(todoList.size()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

    }


    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference(user.getUid()).child("ToDoList").child(today).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        todoList.clear();

                        Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Task task = data.getValue(Task.class);
                            todoList.add(task);
                        }

                        taskListAdapter.forceNotify(todoList);
                        dayTv.setText(String.valueOf(todoList.size()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

}
