package com.kyser.demosuite.view.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.InsightModel;
import com.kyser.demosuite.service.streamservice.StreamService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Insights extends AppCompatActivity implements OnChartValueSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);
        initChart();

    }

    private void initChart() {
        PieChart chart = (PieChart) findViewById(R.id.chart);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
        setLegend(chart);
        getInsights(chart);
    }

    private void getInsights(PieChart chart) {
        StreamService.getInstance().getInsights("subcategory", new StreamService.InsightCallback() {
            @Override
            public void onResult(List<InsightModel> result) {
                setAxis(chart,result);
            }
        });
    }

    private void setLegend(PieChart chart) {
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.WHITE);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
    }

    private void setAxis(PieChart chart,List<InsightModel> result) {
        ArrayList<PieEntry>  entries = new ArrayList();
        Iterator<InsightModel> it = result.iterator();
        InsightModel model;
        while(it.hasNext()){
            model =  it.next();
            entries.add(new PieEntry( model.getCount(),model.getTemplate() ));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Genres viewed");
        chart.animateY(1400, Easing.EaseInOutQuad);

        PieData data = new PieData(dataSet);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(getResources().getColor(R.color.app_bg_dk));
        chart.setData(data);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavBars();
    }

    protected void hideNavBars() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
