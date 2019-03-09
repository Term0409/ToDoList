package com.example.sirishaa.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sirishaa.todo.Adapters.CategoryListAdapter;
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

public class CategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<String> catList;
    static CategoryListAdapter categoryListAdapter;
    static TextView dayTv;
    static TextView titleTv;
    static EditText titleEt;
    static AppCompatButton addTaskB;
    TextView myTasksTv;
    FloatingActionButton add_bt;
    String catValue, today;

    public static void saveTasksInCategories(String taskTitle, String catValue) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String category = database.getReference("ToDoList").child(catValue).push().getKey();


        Map<String, Object> childUpdates1 = new HashMap<>();
        childUpdates1.put(category, taskTitle);
        database.getReference("Categories").child(catValue).updateChildren(childUpdates1, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                }
            }
        });

    }

    public static void saveTodo(String taskTitle, String today, String selectedCategory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("ToDoList").child(today).push().getKey();

        Task taskNew = new Task();
        taskNew.setTaskName(taskTitle);
        taskNew.setCategory(selectedCategory);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, taskNew.toFirebaseObject());
        database.getReference("ToDoList").child(today).updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        dayTv = findViewById(R.id.day_tv);
        add_bt = findViewById(R.id.act_add);
        add_bt.setOnClickListener(this);
        myTasksTv = findViewById(R.id.mytasks_tv);
        Intent i = getIntent();
        catValue = i.getStringExtra("Category");
        myTasksTv.setText(catValue);
        catList = new ArrayList<>();

        Date d = new Date();
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        today = simpleDateFormat.format(d);

        RecyclerView recyclerView = findViewById(R.id.task_rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        categoryListAdapter = new CategoryListAdapter(this);
        recyclerView.setAdapter(categoryListAdapter);
        dayTv.setText(String.valueOf(catList.size()) + "tasks");
        categoryListAdapter.forceNotify(catList);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("Categories").child(catValue).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        catList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String task = data.getValue(String.class);
                            catList.add(task);
                        }

                        categoryListAdapter.forceNotify(catList);
                        dayTv.setText(String.valueOf(catList.size()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.act_add) {
            final AppCompatDialog rejectDlg = new AppCompatDialog(this);

            View view = LayoutInflater.from(this).inflate(R.layout.add_task_dialog_2, null);
            addTaskB = view.findViewById(R.id.add_task_b);
            //titleTv = view.findViewById(R.id.title_tv);
            titleEt = view.findViewById(R.id.title_et);


            addTaskB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskTitle = titleEt.getText().toString();
                    saveTasksInCategories(taskTitle, catValue);
                    saveTodo(taskTitle, today, catValue);
                    rejectDlg.dismiss();
                }
            });
            rejectDlg.setContentView(view);
            rejectDlg.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("Categories").child(catValue).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        catList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.e("VALUEEEE: ", " " + data.getValue(String.class));
                            String task = data.getValue(String.class);
                            catList.add(task);
                        }

                        categoryListAdapter.forceNotify(catList);
                        dayTv.setText(String.valueOf(catList.size()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });
    }


}
