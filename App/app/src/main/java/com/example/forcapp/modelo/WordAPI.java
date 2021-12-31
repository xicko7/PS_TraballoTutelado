package com.example.forcapp.modelo;

import com.example.forcapp.entity.Word;

import java.util.List;

public interface WordAPI {

    Word findWordById(Long wordId);

    void insertWord(Word word);

    void deleteWord(Word word);

    List<Word> getAllWords(Word word);
}
