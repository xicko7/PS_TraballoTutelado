package com.example.forcapp;

import static com.example.forcapp.MainActivity.defaultWordList;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forcapp.database.WordDatabaseClient;
import com.example.forcapp.entity.Word;

import java.util.ArrayList;
import java.util.List;


public class Dictionary extends AppCompatActivity {

    RecyclerView recyclerView;
    private WordsAdapter mAdapter;
    ArrayList<Word> initialData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_layout);
        getSupportActionBar().setTitle(R.string.dicionario);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Engadir as palabras por defecto da app
        setUI();
        insertDefaultWordList(defaultWordList, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter.getItemCount()==0)
            insertDefaultWordList(defaultWordList, false);
    }

    private void setUI() {

        recyclerView = findViewById(R.id.word_rv);


        mAdapter = new WordsAdapter(initialData);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dictionary_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.a??adir_menu:
                createAddDialog(mAdapter);
                return true;
            case R.id.reset_menu:
                createResetBdDialog();
                return true;
            case R.id.deleteall_menu:
                createDeleteAllDialog();
                return true;
            case android.R.id.home:
                // Respond to the action bar's Up/Home button
                if (mAdapter.getItemCount() == 0) {
                    createExitDialog(mAdapter);
                    return true;
                } else
                    return false;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (mAdapter.getItemCount() == 0)
            createExitDialog(mAdapter);
        else
            finish();
    }

    private void insertWord(Word word, WordsAdapter mAdapter) {
        //Controlar que no se repita la palabra
        class InsertWord extends AsyncTask<Void, Void, Word> { // claseinterna
            @Override
            protected Word doInBackground(Void... voids) {
                int finded = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().existsWord(word.getWord());
                if (finded == 0)
                    WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().insertWord(word);
                return word;
            }

            @Override
            protected void onPostExecute(Word word) {
                super.onPostExecute(word);// Actualizar la UI
                showCurrentWords(mAdapter);
            }
        }
        InsertWord gf = new InsertWord(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void insertDefaultWordList(List<Word> wordList, boolean reset) {
        class InsertWordList extends AsyncTask<Void, Void, List<Word>> {
            @Override
            protected List<Word> doInBackground(Void... voids) {
                if (reset) {
                    WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().deleteAllWords();
                }

                //Permite que cada vez que entremos no dicionario e est?? baleiro meta as palabras novamente
                //Con isto conseguimos ter sempre palabras dentro da BBDD, que nos aforrar?? problemas no futuro
                if (WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords().isEmpty()) {
                    WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().insertWordList(wordList);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words);
                showCurrentWords(mAdapter);
            }
        }
        InsertWordList gf = new InsertWordList(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void removeWord(WordsAdapter mAdapter, int pos) {
        class RemoveWord extends AsyncTask<Void, Void, Word> { // claseinterna
            @Override
            protected Word doInBackground(Void... voids) {
                WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().deleteWordByName(mAdapter.getWord(pos).getWord());
                return null;
            }

            @Override
            protected void onPostExecute(Word word) {
                super.onPostExecute(word); // Actualizar la UI
                mAdapter.removeWord(pos);
            }
        }
        RemoveWord gf = new RemoveWord(); // Crear una instancia y ejecutar
        gf.execute();

    }

    private void showCurrentWords(WordsAdapter mAdapter) {
        class GetAllWords extends AsyncTask<Void, Void, List<Word>> { // claseinterna
            @Override
            protected List<Word> doInBackground(Void... voids) {
                List<Word> words = WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().getAllWords();
                return words;
            }

            @Override
            protected void onPostExecute(List<Word> words) {
                super.onPostExecute(words); // Actualizar la UI
                mAdapter.removeWords();
                mAdapter.addAll(words);
            }
        }
        GetAllWords gf = new GetAllWords(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void deleteAllWords() {
        class DeleteAllWords extends AsyncTask<Void, Void, Word> { // claseinterna
            @Override
            protected Word doInBackground(Void... voids) {
                WordDatabaseClient.getInstance(getApplicationContext()).getWordDatabase().getWordDao().deleteAllWords();
                return null;
            }

            @Override
            protected void onPostExecute(Word word) {
                super.onPostExecute(word); // Actualizar la UI
                mAdapter.removeWords();
            }
        }
        DeleteAllWords gf = new DeleteAllWords(); // Crear una instancia y ejecutar
        gf.execute();
    }

    private void createExitDialog(WordsAdapter mAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.exit_dictionary_dialog);

        builder.setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insertDefaultWordList(defaultWordList, true);
            }
        });
        builder.setNegativeButton(R.string.add_word, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createAddDialog(mAdapter);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createAddDialog(WordsAdapter mAdapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_dialog);

        EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.setHint(R.string.add_hint);

        builder.setPositiveButton(R.string.dialog_acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String avaliableLetters = "qwertyuiopasdfghjkl??zxcvbnmQWERTYUIOPASDFGHJKL??ZXCVBNM????????????????????";
                String newWord = String.valueOf(input.getText());
                if (newWord.length() > 17) {
                    Toast.makeText(getApplicationContext(), "Palabra demasiado longa.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newWord.length() <= 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.add_hint), Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean avaliable = true;
                // Comprobar que non hai espazos
                for (int j = 0; j < newWord.length(); j++) {
                    if (!avaliableLetters.contains(String.valueOf(newWord.charAt(j)))) {
                        avaliable = false;
                        Toast.makeText(getApplicationContext(), getString(R.string.add_hint), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (avaliable) {
                    String word = newWord.toUpperCase();
                    insertWord(new Word(word), mAdapter);
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createDeleteDialog(WordsAdapter mAdapter, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remove_dialog);

        builder.setPositiveButton(R.string.dialog_acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeWord(mAdapter, position);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createResetBdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.reset_dialog);

        builder.setPositiveButton(R.string.dialog_acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                insertDefaultWordList(defaultWordList, true);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void createDeleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.deleteall_dialog);

        builder.setPositiveButton(R.string.dialog_acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAllWords();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancelar
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}