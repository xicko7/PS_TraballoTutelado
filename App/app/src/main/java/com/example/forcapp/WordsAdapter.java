package com.example.forcapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.MyViewHolder> {
    private final ArrayList<Word> mDataset;
    private static OnItemClickListener clickListener;
    String palabra;

    public WordsAdapter(ArrayList<Word> myDataset) {
        mDataset = myDataset;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public Button removeButton;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.word);
            view.setOnClickListener(this);
            removeButton = view.findViewById(R.id.remove_word);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = 1;
                    // Aquí débese implementar unha búsqueda na base de datos para obter o índice da palabra
                    removeWord(i);
                }
            });
        }

        public void bind(Word word) {
            title.setText(word.getTitle());
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    @NonNull
    @Override
    public WordsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.word_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    void removeWord(int i){
        mDataset.remove(i);
        notifyDataSetChanged();
    }

    void addWord(String text){
            Word word = new Word(text);
            mDataset.add(word);
            notifyItemInserted(mDataset.size() - 1);
     }

    Word getWord(int position){
        return mDataset.get(position);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}