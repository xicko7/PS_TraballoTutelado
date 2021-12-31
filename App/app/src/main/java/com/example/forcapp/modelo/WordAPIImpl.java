package com.example.forcapp.modelo;

import com.example.forcapp.dao.WordDao;
import com.example.forcapp.entity.Word;

import java.util.List;

public class WordAPIImpl implements WordAPI {

    WordDao dao;

    public WordAPIImpl(WordDao dao) {
        this.dao = dao;
    }

    @Override
    public Word findWordById(Long wordId) {
        return dao.getWord(wordId);
    }

    @Override
    public void insertWord(Word word) {
        dao.insertWord(word);
    }

    @Override
    public void deleteWord(Word word) {
        dao.deleteWord(word);
    }

    @Override
    public List<Word> getAllWords(Word word) {
        return dao.getAllWords();
    }
}
