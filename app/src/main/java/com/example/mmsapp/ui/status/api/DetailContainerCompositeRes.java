package com.example.mmsapp.ui.status.api;

import com.example.mmsapp.ui.status.model.Material;
import com.google.gson.annotations.SerializedName;

public class DetailContainerCompositeRes {

    @SerializedName("result")
    private boolean result;

    @SerializedName("number")
    private int number;

    @SerializedName("message")
    private String message;

    @SerializedName("datamachine")
    private Material material;

    public DetailContainerCompositeRes(boolean result, int number, String message, Material material) {
        this.result = result;
        this.number = number;
        this.message = message;
        this.material = material;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
