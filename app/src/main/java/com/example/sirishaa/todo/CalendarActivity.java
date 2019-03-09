package com.example.sirishaa.todo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.sirishaa.todo.Adapters.CategoryAdapter;
import com.example.sirishaa.todo.Adapters.EventListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.sirishaa.todo.MainActivity.user;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    FloatingActionButton add_bt;
    static EventListAdapter eventListAdapter;
    public static ArrayList<Task> todoList;
    String selectedDate, selectedCategory;
    int listPosition = 0;

    static TextView titleTv;
    static EditText titleEt;
    static AppCompatButton addTaskB;

    private static RecyclerView horizontal_rv;
    private static ArrayList<String> categoryList;
    private static CategoryAdapter categoryAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        add_bt = findViewById(R.id.add_event);
        add_bt.setOnClickListener(this);

        View calendarFrag = findViewById(R.id.calendar1);
        calendarFrag.setOnClickListener(this);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        todoList = new ArrayList<>();
        //List of events
        RecyclerView recyclerView = findViewById(R.id.events_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        eventListAdapter = new EventListAdapter();
        recyclerView.setAdapter(eventListAdapter);
        eventListAdapter.forceNotify(todoList);

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                caldroidFragment.moveToDate(date);
                caldroidFragment.refreshView();
                String pattern = "MM-dd-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                selectedDate = simpleDateFormat.format(date);
                Toast.makeText(getApplicationContext(), selectedDate,
                        Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                database.getReference(user.getUid()).child("ToDoList").child(selectedDate).addListenerForSingleValueEvent(
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

                                eventListAdapter.forceNotify(todoList);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }


        };

        caldroidFragment.setCaldroidListener(listener);




    }


    public void getSelectedCategory(int listPosition)
    {
        this.listPosition = listPosition;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.add_event)

        {
                final AppCompatDialog rejectDlg = new AppCompatDialog(this);
                categoryList = new ArrayList<>();
                categoryList.add("Work");
                categoryList.add("Personal");
                categoryList.add("Shopping");

                View view = LayoutInflater.from(this).inflate(R.layout.add_task_dialog, null);
                horizontal_rv= view.findViewById(R.id.categories_rv);
                addTaskB = view.findViewById(R.id.add_task_b);
               // titleTv = (TextView)view.findViewById(R.id.title_tv);
                titleEt = view.findViewById(R.id.title_et);
                horizontal_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,true));
                categoryAdapter=new CategoryAdapter(categoryList, this);
                horizontal_rv.setAdapter(categoryAdapter);

                addTaskB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String taskTitle = titleEt.getText().toString();
                        selectedCategory = categoryList.get(listPosition);
                        saveEvent(taskTitle,selectedDate,selectedCategory);
                        rejectDlg.dismiss();
                    }
                });
                rejectDlg.setContentView(view);
                rejectDlg.show();
        }
    }

    public static void saveEvent(String taskTitle, String today, String selectedCategory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference(user.getUid()).child("ToDoList").child(today).push().getKey();

        Task taskNew = new Task();
        taskNew.setTaskName(taskTitle);
        taskNew.setCategory(selectedCategory);

        //taskNew.setCategory(categoryList.get(i).getText().toString());

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, taskNew.toFirebaseObject());
        database.getReference("ToDoList").child(today).updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
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

                        eventListAdapter.forceNotify(todoList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

    }




}
