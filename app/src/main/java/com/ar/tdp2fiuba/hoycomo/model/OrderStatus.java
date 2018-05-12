package com.ar.tdp2fiuba.hoycomo.model;

import android.content.Context;

import com.ar.tdp2fiuba.hoycomo.R;

public enum OrderStatus {
    TAKEN(R.string.taken),
    IN_PREPARATION(R.string.in_preparation),
    DISPATCHED(R.string.dispatched),
    DELIVERED(R.string.delivered),
    CANCELLED(R.string.cancelled);

    private int i18nId;

    OrderStatus(int i18nId) {
        this.i18nId = i18nId;
    }

    public String toString(Context context) {
        return context.getString(i18nId);
    }
}
