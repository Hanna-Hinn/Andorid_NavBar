package com.example.ass_activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class activity_grades extends AppCompatActivity {

    Gson gson = new Gson();

    private Button addGrade;
    private AlertDialog dialog;
    private LinearLayout layout;

    private SharedPreferences prefs;
    private android.content.SharedPreferences.Editor editor;
    public static final boolean FLAG = true;

    private ArrayList<ViewGrade> viewGrades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        addGrade = findViewById(R.id.addGrade);
        layout = findViewById(R.id.gradeContainer);

        setUpSharedPreferences();
//        editor.clear();
//        editor.commit();
        checkData();

        buildDialog();

        addGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void setUpSharedPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void save() {


        if (!viewGrades.isEmpty()) {
            String strList = gson.toJson(viewGrades);
            editor.putBoolean("FLAG", FLAG);
            editor.putString("grades", strList);
            editor.apply();
        }

    }

    private void checkData() {
        boolean flag = prefs.getBoolean("FLAG", false);
        if (flag) {
            String strList = prefs.getString("grades", null);
            Type listType = new TypeToken<ArrayList<Grade>>() {
            }.getType();
            viewGrades = gson.fromJson(strList, listType);

            for (ViewGrade grade : viewGrades) {
                addCard(grade.getSubject(), grade.getGrade());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewGrades.add(new ViewGrade("TEST!"));
        save();
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_grade, null);

        final EditText name = view.findViewById(R.id.addSubName);

        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewGrade viewGrade = new ViewGrade(name.getText().toString());
                        viewGrades.add(viewGrade);
                        addCard(name.getText().toString(), 0);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }

    private void addCard(String name, float viewGrade) {
        final View view = getLayoutInflater().inflate(R.layout.card_grade, null);

        TextView subject = view.findViewById(R.id.subjectName);
        TextView grade = view.findViewById(R.id.subjectGrade);
        Button delete = view.findViewById(R.id.gradeDelete);

        subject.setText(name);
        grade.setText(String.valueOf(viewGrade));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ViewGrade x : viewGrades) {
                    if (x.getSubject() == subject.getText().toString()) {
                        viewGrades.remove(x);
                        break;
                    }
                }
                layout.removeView(view);
            }
        });
        layout.addView(view);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}