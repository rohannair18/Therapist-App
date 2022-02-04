package com.example.etherealtherapist.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etherealtherapist.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.datepicker.CalendarConstraints;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    CompactCalendarView compactCalendarView;
    TextView monthyear;
    private SimpleDateFormat dateFormatmonth=new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        compactCalendarView=view.findViewById(R.id.compactcalendar_view);
        monthyear=view.findViewById(R.id.MonthYear);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getContext();

                if (dateClicked.toString().compareTo("Sat Oct 21 00:00:00 AST 2022") == 0) {
                    Toast.makeText(context, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthyear.setText(dateFormatmonth.format(firstDayOfNewMonth));
            }
        });
        return view;
    }
}