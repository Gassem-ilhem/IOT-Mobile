package com.example.iotfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.dataentry.SeriesDataEntry;
import java.util.ArrayList;
import java.util.List;

public class LineActivity extends AppCompatActivity {

    LineChart lineChart;
    AnyChartView anyChartView;
    Cartesian cartesian;
    List<DataEntry> seriesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        DataSet dataSet = anyChartView.dataSet();
        Series series = dataSet.mapAs("{ x: 'x', value: 'value' }");

        cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(5d, 2d, 15d, 2d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Trend of Temperature .");

        cartesian.yAxis(0).title("°C");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        seriesData = new ArrayList<>();

        Temp temp = new Temp();
        temp.FetTemps(LineActivity.this, new TempsCallback() {
            @Override
            public void callback(ArrayList<DataEntry> temps) {
                Log.d("firebase", "refreshed");

                seriesData.clear();
                seriesData.addAll(temps);

                Set set = Set.instantiate();
                set.data(seriesData);
                Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

                Line series1 = cartesian.line(series1Mapping);
                series1.name("Temperature");
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);

                cartesian.legend().enabled(true);
                cartesian.legend().fontSize(13d);
                cartesian.legend().padding(0d, 0d, 10d, 0d);
                anyChartView.setChart(cartesian);
                anyChartView.refresh();
            }
        });
    }
}
