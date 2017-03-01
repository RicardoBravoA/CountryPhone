package com.rba.countryphone.component.model;

import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by Ricardo Bravo on 17/02/17.
 */

public class CountryEntity {

    private final String code;

    private final String name;

    private final int dialCode;

    public CountryEntity(String code, String name, int dialCode) {
        this.code = code;
        this.name = name;
        this.dialCode = dialCode;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getDialCode() {
        return dialCode;
    }

    public String getDisplayName() {
        return new Locale("", code).getDisplayCountry(Locale.getDefault());
    }

    public int getResId(Context context) {
        String description = String.format("country_flag_%s", code.toLowerCase());
        final Resources resources = context.getResources();
        return resources.getIdentifier(description, "drawable", context.getPackageName());
    }
}
