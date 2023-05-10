package com.example.iotfinal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.enums.Anchor;
import com.anychart.graphics.vector.text.HAlign;

public class TempReelActivity extends AppCompatActivity {

    private CircularGauge circularGauge;
    private double currentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        circularGauge = AnyChart.circular();

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        // Create the initial chart
        createChart();

        // Schedule the updates every 5 seconds
        Handler handler = new Handler();
        handler.postDelayed(updateRunnable, 5000);
    }

    private void createChart() {
        // Create the chart with initial values
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0, 0, 0, 0)
                .margin(30, 30, 30, 30);
        circularGauge.startAngle(0)
                .sweepAngle(360);

        double currentValue = getRandomValue();
        circularGauge.data(new SingleValueDataSet(new Double[] { currentValue }));

        circularGauge.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{ type: 'line', length: 4, position: 'outside' }");

        circularGauge.axis(0).labels().position("outside");

        circularGauge.axis(0).scale()
                .minimum(0)
                .maximum(140);

        circularGauge.axis(0).scale()
                .ticks("{interval: 10}")
                .minorTicks("{interval: 10}");

        circularGauge.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("38%")
                .startWidth("2%")
                .endWidth(0);

        circularGauge.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        circularGauge.label(0)
                .text("<span style=\"font-size: 25\">Temperature</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);

        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + currentValue + "</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER);
        circularGauge.label(1)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(-100)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");

        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 25,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'green 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        circularGauge.range(1,
                "{\n" +
                        "    from: 80,\n" +
                        "    to: 140,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setChart(circularGauge);
    }

    private void updateChart() {
        // Update the chart with a new random value
        double newValue = getRandomValue();
        Temp temp = new Temp();
        temp.FetLastTemp(TempReelActivity.this, new TempCallback2() {
            @Override
            public void callback(float value) {
                double double_val = (double) value;
                circularGauge.data(new SingleValueDataSet(new Double[] {Double.valueOf(value)}));
                circularGauge.label(1).text("<span style=\"font-size: 20\">" + value + "</span>");
            }
        });

    }

    private double getRandomValue() {
        // Generate a random value between 0 and 140 with two decimal places
        double random = Math.random() * 14000; // Generate a random value between 0 and 14000
        return Math.round(random) / 100.0; // Round the value to two decimal places
    }


    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateChart();
            // Schedule the next update after 2 seconds
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(updateRunnable, 2000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending update callbacks when the activity is destroyed
        Handler handler = new Handler(Looper.getMainLooper());
        handler.removeCallbacks(updateRunnable);
    }
}

