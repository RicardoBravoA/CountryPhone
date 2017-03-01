package com.rba.countryphone.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.rba.countryphone.R;
import com.rba.countryphone.component.adapter.CountryAdapter;
import com.rba.countryphone.component.model.Countries;
import com.rba.countryphone.component.model.CountryEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.OnTouch;

/**
 * Created by Ricardo Bravo on 17/02/17.
 */

public abstract class PhoneField extends LinearLayout {

    @BindView(R.id.spCountry) AppCompatSpinner spCountry;
    @BindView(R.id.txtNumber) AppCompatEditText txtNumber;
    private CountryEntity mCountryEntity;
    private PhoneNumberUtil mPhoneUtil = PhoneNumberUtil.getInstance();
    private static final String TAG = PhoneField.class.getName();
    private CountryAdapter adapter;
    private String hint;

    public PhoneField(Context context) {
        this(context, null);
    }

    public PhoneField(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhoneField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), getLayoutResId(), this);
        updateLayoutAttributes();
        prepareView(context, attrs);
    }

    protected void prepareView(Context context, AttributeSet attrs) {
        ButterKnife.bind(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PhoneText, 0, 0);

        if (a != null) {
            hint = a.getString(R.styleable.PhoneText_showHint);
            setHint(hint);
            a.recycle();
        }

        if (spCountry == null || txtNumber == null) {
            throw new IllegalStateException("Please provide a valid xml layout");
        }

        adapter = new CountryAdapter(getContext(), Countries.COUNTRIES);

        spCountry.setAdapter(adapter);
    }

    @OnItemSelected(R.id.spCountry)
    public void spinnerItemSelected(int position) {
        mCountryEntity = adapter.getItem(position);
    }

    @OnItemSelected(value = R.id.spCountry, callback = OnItemSelected.Callback.NOTHING_SELECTED)
    public void onNothingSelected(){
        mCountryEntity = null;
    }


    @OnTouch(R.id.spCountry)
    public boolean onTouchSpinner(){
        hideKeyboard();
        return false;
    }

    public AppCompatEditText onTextChanged(){
        return txtNumber;
    }

    public Spinner getSpinner() {
        return spCountry;
    }

    public EditText getEditText() {
        return txtNumber;
    }

    public boolean isValid() {
        try {
            return mPhoneUtil.isValidNumber(parsePhoneNumber(getRawInput()));
        } catch (NumberParseException e) {
            Log.i(TAG, "isValid: "+e.getMessage());
            return false;
        }
    }

    private Phonenumber.PhoneNumber parsePhoneNumber(String number) throws NumberParseException {
        String defaultRegion = mCountryEntity != null ? mCountryEntity.getCode().toUpperCase() : "";
        return mPhoneUtil.parseAndKeepRawInput(number, defaultRegion);
    }

    public String getPhoneNumber() {
        try {
            Phonenumber.PhoneNumber number = parsePhoneNumber(getRawInput());
            return mPhoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException ignored) {
            Log.i(TAG, "getPhoneNumber: "+ignored.getMessage());
        }
        return getRawInput();
    }

    public void setDefaultCountry(String countryCode) {
        for (int i = 0; i < Countries.COUNTRIES.size(); i++) {
            CountryEntity countryEntity = Countries.COUNTRIES.get(i);
            if (countryEntity.getCode().equalsIgnoreCase(countryCode)) {
                mCountryEntity = countryEntity;
                spCountry.setSelection(i);
            }
        }
    }

    private void selectCountry(int dialCode) {
        for (int i = 0; i < Countries.COUNTRIES.size(); i++) {
            CountryEntity countryEntity = Countries.COUNTRIES.get(i);
            if (countryEntity.getDialCode() == dialCode) {
                mCountryEntity = countryEntity;
                spCountry.setSelection(i);
            }
        }
    }

    public void setPhoneNumber(String rawNumber) {
        try {
            Phonenumber.PhoneNumber number = parsePhoneNumber(rawNumber);
            if (mCountryEntity == null || mCountryEntity.getDialCode() != number.getCountryCode()) {
                selectCountry(number.getCountryCode());
            }
            txtNumber.setText(mPhoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException ignored) {
            Log.i(TAG, "setPhoneNumber: "+ignored.getMessage());
        }
    }

    private void hideKeyboard() {
        ((InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(txtNumber.getWindowToken(), 0);
    }

    protected abstract void updateLayoutAttributes();

    public abstract int getLayoutResId();

    public void setHint(String hint) {
        txtNumber.setHint(hint);
    }

    public void setHint(int resId) {
        txtNumber.setHint(resId);
    }

    public String getRawInput() {
        return txtNumber.getText().toString();
    }

    public void setError(String error) {
        txtNumber.setError(error);
    }

    public void setTextColor(int resId) {
        txtNumber.setTextColor(resId);
    }

}