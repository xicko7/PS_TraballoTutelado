package com.example.forcapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Dictionary extends AppCompatActivity {

    RecyclerView recyclerView;
    Button add;
    private WordsAdapter mAdapter;
    int added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_layout);

        recyclerView = findViewById(R.id.word_rv);
        add = findViewById(R.id.button_addWord);

        ArrayList<Word> initialData = new ArrayList<>();

//        Engadir primeiros elementos
        for (int i = 0; i < 10; i++)
            initialData.add(new Word(Integer.toString(i)));

        mAdapter = new WordsAdapter (initialData);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener(new WordsAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                createDeleteDialog(mAdapter, position);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddDialog(mAdapter);
            }
        });

    }

    private void createAddDialog(WordsAdapter mAdapter){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_dialog);

        EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton(R.string.dialogOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),R.string.dialogOK, Toast.LENGTH_SHORT).show();
                added =  Integer.parseInt(input.getText().toString());
                mAdapter.addWord(String.valueOf(input.getText()));
            }
        });
        builder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),R.string.dialogCancel, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createDeleteDialog(WordsAdapter mAdapter, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remove_dialog);

        builder.setPositiveButton(R.string.dialogOK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAdapter.removeWord(position);
            }
        });
        builder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}