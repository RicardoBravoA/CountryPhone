package com.rba.countryphone.component.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.rba.countryphone.R;
import com.rba.countryphone.component.model.CountryEntity;

import java.util.List;

/**
 * Created by Ricardo Bravo on 17/02/17.
 */

public class CountryAdapter extends ArrayAdapter<CountryEntity> implements SpinnerAdapter {

    private final LayoutInflater mInflater;

    public CountryAdapter(Context context, List<CountryEntity> countries) {
        super(context, R.layout.item_country, R.id.lblName, countries);
        mInflater = LayoutInflater.from(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        CountryEntity countryEntity = getItem(position);
        if (view == null) {
            view = mInflater.inflate(R.layout.spinner_country, parent, false);
        }
        AppCompatImageView imgCountryInit = (AppCompatImageView) view.findViewById(R.id.imgCountryInit);
        imgCountryInit.setImageResource(countryEntity.getResId(getContext()));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_country, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.lblName = (AppCompatTextView) view.findViewById(R.id.lblName);
            viewHolder.lblCode = (AppCompatTextView) view.findViewById(R.id.lblCode);
            viewHolder.imgCountry = (AppCompatImageView) view.findViewById(R.id.imgCountry);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CountryEntity countryEntity = getItem(position);
        viewHolder.imgCountry.setImageResource(countryEntity.getResId(getContext()));
        viewHolder.lblName.setText(countryEntity.getDisplayName());
        viewHolder.lblCode.setText(String.valueOf(countryEntity.getDialCode()));
        return view;

    }

    private static class ViewHolder {
        AppCompatTextView lblName;
        AppCompatTextView lblCode;
        AppCompatImageView imgCountry;
    }
}
