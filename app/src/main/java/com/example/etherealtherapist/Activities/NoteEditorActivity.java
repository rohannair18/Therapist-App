package com.example.etherealtherapist.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.etherealtherapist.Fragments.JournalFragment;
import com.example.etherealtherapist.R;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        EditText editText=(EditText) findViewById(R.id.EditText);
        Intent intent=getIntent();
        noteId= intent.getIntExtra("noteID",-1);
        if(noteId!=-1){
           editText.setText(JournalFragment.notes.get(noteId));
        }else {
            JournalFragment.notes.add("");
            noteId=JournalFragment.notes.size()-1;
            JournalFragment.arrayAdapter.notifyDataSetChanged();
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   JournalFragment.notes.set(noteId, String.valueOf(charSequence));
                   JournalFragment.arrayAdapter.notifyDataSetChanged();
                   SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.etherealtherapist.Activities.NoteEditorActivity", Context.MODE_PRIVATE);
                   HashSet<String> set= new HashSet(JournalFragment.notes);
                   sharedPreferences.edit().putStringSet("notes",set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}