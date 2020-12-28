package com.example.dl.Reports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dl.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ExpenseReport extends AppCompatActivity {
    TextView monthText;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        //Hooks
        monthText = findViewById(R.id.monthText);
        pieChart = findViewById(R.id.epPieChart);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Add month
        String month = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(new Date());
        monthText.setText(month);

        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(22,"Expenses"));
        data.add(new PieEntry(78,"Profit"));

        PieDataSet pieDataSet = new PieDataSet(data,"Expenses and Profit");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(22f);

        PieData newPieData = new PieData(pieDataSet);

        pieChart.setData(newPieData);
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText("Monthly Revenue");
        pieChart.animate();
    }
}