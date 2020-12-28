package com.example.dl.Reports;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dl.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS;

public class CustomerReport extends AppCompatActivity {

    BarChart customerBarChart;
    TextView monthText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report);

        //Hide StatusBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Hooks
        monthText = findViewById(R.id.monthText);
        customerBarChart = findViewById(R.id.customerChart);

        //Add month
        String month = new SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(new Date());
        monthText.setText(month);

        customerBarChart= findViewById(R.id.customerChart);
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(01, 111));
        data.add(new BarEntry(02, 222));
        data.add(new BarEntry(03, 333));

        BarDataSet dataSet = new BarDataSet(data, "Customer Report");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(20f);

        BarData barData= new BarData(dataSet);

        customerBarChart.setFitBars(true);
        customerBarChart.setData(barData);
        customerBarChart.getDescription().setText("Data");
        customerBarChart.animateY(2000);
    }
}