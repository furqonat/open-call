package com.furqonr.opencall.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class UIDWrapper implements Serializable {

    private final String uid;

    public UIDWrapper(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    @NonNull
    @Override
    public String toString() {
        return uid;
    }
}
