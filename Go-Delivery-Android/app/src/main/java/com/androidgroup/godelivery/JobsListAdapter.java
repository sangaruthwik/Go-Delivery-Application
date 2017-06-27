package com.androidgroup.godelivery;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class JobsListAdapter extends BaseAdapter {

    Context context;
    String[] data;
    private static LayoutInflater inflater = null;

    List<String> jobIDsList = new ArrayList<String>();

    List<String> productNamesList = new ArrayList<String>();
    List<String> distanceList = new ArrayList<String>();
    List<String> rateList = new ArrayList<String>();

    Typeface font;

    public JobsListAdapter(Context context, List<String> productNamesList, List<String> distanceList, List<String> rateList) {
        // UDO Auto-generated constructor stub
        this.context = context;
        this.productNamesList = productNamesList;
        this.distanceList = distanceList;
        this.rateList = rateList;
        font = Typeface.createFromAsset(context.getAssets(), "FancyFont_1.ttf");



        //this.jobIDsList = jobIDsList;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return productNamesList.size();
    }

    @Override
    public Object getItem(int position) {
        //return data[position];
        return productNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);



        Double distanceDoubleFormat = Double.valueOf(distanceList.get(position));
        double roundDistance = (double) Math.round(distanceDoubleFormat * 100.0) / 100.0;
        String roundedDistance = String.valueOf(roundDistance);

        Double amountDoubleFormat = Double.valueOf(rateList.get(position));
        double roundAmount = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;
        String roundedAmount = String.valueOf(roundAmount);

        TextView text1 = (TextView) vi.findViewById(R.id.ListProductNameID);
        text1.setText(productNamesList.get(position));
        text1.setTypeface(font);
        text1.setTextColor(Color.BLACK);

        TextView text2 = (TextView) vi.findViewById(R.id.ListDistanceID);
        text2.setText(roundedDistance + " KM");
        text2.setTypeface(font);
        text2.setTextColor(Color.BLACK);

        TextView text3 = (TextView) vi.findViewById(R.id.ListRateID);
        text3.setText("€" + roundedAmount);
        text3.setTypeface(font);
        text3.setTextColor(Color.BLACK);


        return vi;
    }

    public void updateReceiptsList(List<String> productNames, List<String> distance, List<String> rate) {

        productNamesList = productNames;
        distanceList = distance;
        rateList = rate;

        this.notifyDataSetChanged();
    }

}
