package com.example.iotfinal;


import com.anychart.chart.common.dataentry.DataEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;


public interface TempsCallback {

    void callback(ArrayList<DataEntry> entry);
}
