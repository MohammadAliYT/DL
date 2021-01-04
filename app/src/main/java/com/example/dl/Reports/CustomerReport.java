package com.example.dl.Reports;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dl.ChartHelpers.MonthHelper;
import com.example.dl.Contacts.ContactList;
import com.example.dl.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS;

public class CustomerReport extends AppCompatActivity {

    TextView monthText;
    ArrayList<MonthHelper> monthlySalesDataArrayList = new ArrayList<>();
    BarChart barChart;
    ArrayList<BarEntry> barEntriesArrayList;
    ArrayList<String> lableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report);
        monthlySale();

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        monthText = findViewById(R.id.monthText);
        barChart = findViewById(R.id.customerChart);

        //Add month
        String month = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(new Date());
        monthText.setText(month);

        barChart = findViewById(R.id.customerChart);
        barEntriesArrayList = new ArrayList<>();
        lableName = new ArrayList<>();

        for (int i = 0; i < monthlySalesDataArrayList.size(); i++) {
            String monthView = monthlySalesDataArrayList.get(i).getMonth();
            int sales = monthlySalesDataArrayList.get(i).getSales();
            barEntriesArrayList.add(new BarEntry(i, sales));
            lableName.add(monthView);
        }

        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Monthly Sales");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Months");
        barChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xAxis =barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(lableName));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(lableName.size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(2000);
        barChart.invalidate();

    }

    public void goToReports(View view) {
        startActivity(new Intent(getApplicationContext(), Reports.class));
        finish();
    }

    private void monthlySale() {
        monthlySalesDataArrayList.clear();
        monthlySalesDataArrayList.add(new MonthHelper("January", 10000));
        monthlySalesDataArrayList.add(new MonthHelper("February", 23902));
        monthlySalesDataArrayList.add(new MonthHelper("March", 76439));
        monthlySalesDataArrayList.add(new MonthHelper("April", 12399));
        monthlySalesDataArrayList.add(new MonthHelper("May", 56789));
        monthlySalesDataArrayList.add(new MonthHelper("Jun", 123456));
        monthlySalesDataArrayList.add(new MonthHelper("July", 76890));
        monthlySalesDataArrayList.add(new MonthHelper("August", 23455));
        monthlySalesDataArrayList.add(new MonthHelper("September", 45677));
        monthlySalesDataArrayList.add(new MonthHelper("October", 23235));
        monthlySalesDataArrayList.add(new MonthHelper("November", 98732));
        monthlySalesDataArrayList.add(new MonthHelper("December", 20000));
    }
}