package com.example.dl.Trends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dl.ChartHelpers.MonthHelper;
import com.example.dl.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ProductTrend extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Variables
    Spinner productSpinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> productSpinnerData;
    LinearLayout chart1data;

    //First Chart
    ArrayList<MonthHelper> monthlySalesDataArrayList = new ArrayList<>();
    BarChart barChart;
    ArrayList<BarEntry> barEntriesArrayList;
    ArrayList<String> lableName;

    //Second Chart
    ArrayList<MonthHelper> monthlySalesDataArrayList1 = new ArrayList<>();
    BarChart barChart1;
    ArrayList<BarEntry> barEntriesArrayList1;
    ArrayList<String> lableName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_trend);

        monthlySale();
        monthlySale1();

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        productSpinner = findViewById(R.id.productSpinner);
        barChart = findViewById(R.id.chart1);
        barChart1 = findViewById(R.id.chart2);
        chart1data = findViewById(R.id.productData1);

        //Initializing Array
        productSpinnerData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ProductTrend.this, android.R.layout.simple_spinner_dropdown_item, productSpinnerData);

        //Populating Spinner
        ArrayAdapter<CharSequence> productName = ArrayAdapter.createFromResource(this, R.array.product_name, android.R.layout.simple_spinner_item);
        productName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(productName);
        productSpinner.setOnItemSelectedListener(this);

        //Initializing Data for 1st Bar Chart
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
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(lableName));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(lableName.size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(2000);
        barChart.invalidate();

        //
        ///
        ////

        //Initializing Data for 2nd Bar Chart
        barEntriesArrayList1 = new ArrayList<>();
        lableName1 = new ArrayList<>();

        for (int i = 0; i < monthlySalesDataArrayList1.size(); i++) {
            String monthView2 = monthlySalesDataArrayList1.get(i).getMonth();
            int sales = monthlySalesDataArrayList1.get(i).getSales();
            barEntriesArrayList1.add(new BarEntry(i, sales));
            lableName1.add(monthView2);
        }

        BarDataSet barDataSet1 = new BarDataSet(barEntriesArrayList1, "Monthly Sales");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description1 = new Description();
        description1.setText("Months");
        barChart1.setDescription(description1);
        BarData barData1 = new BarData(barDataSet1);
        barChart1.setData(barData1);
        XAxis xAxis1 = barChart1.getXAxis();
        xAxis1.setValueFormatter(new IndexAxisValueFormatter(lableName1));
        xAxis1.setDrawGridLines(false);
        xAxis1.setDrawAxisLine(false);
        xAxis1.setGranularity(1f);
        xAxis1.setLabelCount(lableName1.size());
        xAxis1.setLabelRotationAngle(270);
        barChart1.animateY(2000);
        barChart1.invalidate();
    }

    public void goToTrends(View view) {
        startActivity(new Intent(getApplicationContext(), Trends.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Toast.makeText(getApplicationContext(), "Please Select a customer", Toast.LENGTH_SHORT).show();
        }
        if (position == 1) {
            if (chart1data.getVisibility() == View.GONE) {
                chart1data.setVisibility(View.VISIBLE);
            }
        }
        else {
            String text = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    private void monthlySale1() {
        monthlySalesDataArrayList1.clear();
        monthlySalesDataArrayList1.add(new MonthHelper("January", 10000));
        monthlySalesDataArrayList1.add(new MonthHelper("February", 23902));
        monthlySalesDataArrayList1.add(new MonthHelper("March", 76439));
        monthlySalesDataArrayList1.add(new MonthHelper("April", 12399));
        monthlySalesDataArrayList1.add(new MonthHelper("May", 56789));
        monthlySalesDataArrayList1.add(new MonthHelper("Jun", 123456));
        monthlySalesDataArrayList1.add(new MonthHelper("July", 76890));
        monthlySalesDataArrayList1.add(new MonthHelper("August", 23455));
        monthlySalesDataArrayList1.add(new MonthHelper("September", 45677));
        monthlySalesDataArrayList1.add(new MonthHelper("October", 23235));
        monthlySalesDataArrayList1.add(new MonthHelper("November", 98732));
        monthlySalesDataArrayList1.add(new MonthHelper("December", 20000));
    }
}