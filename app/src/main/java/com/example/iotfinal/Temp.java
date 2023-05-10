package com.example.iotfinal;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.anychart.chart.common.dataentry.DataEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;



public class Temp implements Serializable {
    String date;
    String time;
    float value;

    public Temp(String date, String time, float value) {
        this.date = date;
        this.time = time;
        this.value = value;
    }

    public Temp() {
    }

    /*public void ParseJson(String dataStr) throws JSONException {
        JSONObject jsonResponse = new JSONObject(dataStr);

        // Extract the flight offers array
        JSONArray flightOffers = jsonResponse.getJSONArray("data");

        // Loop through the flight offers and extract the flight data
        for (int i = 0; i < flightOffers.length(); i++) {
            Log.d("msg i =", String.valueOf(i));
            JSONObject flightOffer
    }*/

    public void FetTemps(Context context, TempsCallback callbacks ){
        ArrayList<DataEntry> temps = new ArrayList<DataEntry>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tempBdRef = database.getReference("temps");
        Query query = tempBdRef.limitToLast(10);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int count=0;
                temps.clear();
                for (DataSnapshot snap_data : snapshot.getChildren()) {
                    String date = snap_data.child("date").getValue(String.class);
                    Float value = snap_data.child("value").getValue(Float.class);
                    String time = snap_data.child("time").getValue(String.class);

                    CustomDataEntry temp = new CustomDataEntry(time,value);
                    temps.add(temp);
                    count++;
                    if(snapshot.getChildrenCount() == count){
                       callbacks.callback(temps);

                    }
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void FetLastTemp(Context context, TempCallback2 callbacks ){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tempBdRef = database.getReference("temps");
        Query query = tempBdRef.limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap_data : snapshot.getChildren()) {
                    String date = snap_data.child("date").getValue(String.class);
                    Float value = snap_data.child("value").getValue(Float.class);
                    String time = snap_data.child("time").getValue(String.class);

                    callbacks.callback(value);

                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public String toString() {
        return "Temp{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", value=" + value +
                '}';
    }
}
