package com.example.etherealtherapist.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.view.ActionBarPolicy;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.etherealtherapist.Activities.NoteEditorActivity;
import com.example.etherealtherapist.R;

import java.util.ArrayList;


public class JournalFragment extends Fragment {


    public static ArrayList<String> notes= new ArrayList<>();
    public static ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_journal, container, false);
        View view=inflater.inflate(R.layout.fragment_journal,container,false);
        ListView listView= view.findViewById(R.id.listview);
        notes.add("Example Note");
        arrayAdapter= new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent= new Intent(getContext(), NoteEditorActivity.class);
                intent.putExtra("noteID",i);
                startActivity(intent);
            }
        });
        return view;

    }
}