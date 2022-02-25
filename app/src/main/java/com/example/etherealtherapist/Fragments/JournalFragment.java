package com.example.etherealtherapist.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.etherealtherapist.Activities.NoteEditorActivity;
import com.example.etherealtherapist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;


public class JournalFragment extends Fragment {


    public static ArrayList<String> notes= new ArrayList<>();
    public static ArrayAdapter arrayAdapter;
    FloatingActionButton floatingActionButton;
//    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
//        menuInflater= getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.add_note_menu,menu);
//        super.onCreateOptionsMenu(menu, menuInflater);
//    }
//    public boolean onOptionsItemSelected(MenuItem menuItem){
//        super.onOptionsItemSelected(menuItem);
//        if(menuItem.getItemId()== R.id.add_note){
//            Intent intent= new Intent(getContext(),NoteEditorActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return false;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_journal, container, false);
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.fragment_journal,container,false);
        ListView listView= view.findViewById(R.id.listview);
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("com.example.etherealtherapist.Activities.NoteEditorActivity",Context.MODE_PRIVATE);
        HashSet<String> set= (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if (set==null) {
            notes.add("Example Note");
        }else{
            notes= new ArrayList(set);
        }

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
        floatingActionButton = view.findViewById(R.id.fab_add_notes);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NoteEditorActivity.class);
                startActivity(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete=i;
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences= getContext().getSharedPreferences("com.example.etherealtherapist.Activities.NoteEditorActivity", Context.MODE_PRIVATE);
                                HashSet<String> set= new HashSet(JournalFragment.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
        return view;

    }
}