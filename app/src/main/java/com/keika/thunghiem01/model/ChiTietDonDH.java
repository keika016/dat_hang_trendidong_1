package com.keika.thunghiem01.model;

import java.io.Serializable;

/**
 * Created by Son-Auto on 4/6/2018.
 */

public class ChiTietDonDH implements Serializable {
    private int idDDH;
    private int idSP;
    private int soLuong;

    public ChiTietDonDH() {
    }

    public ChiTietDonDH(int idDDH, int idSP, int soLuong) {
        this.idDDH = idDDH;
        this.idSP = idSP;
        this.soLuong = soLuong;
    }

    public int getIdDDH() {
        return idDDH;
    }

    public void setIdDDH(int idDDH) {
        this.idDDH = idDDH;
    }

    public int getIdSP() {
        return idSP;
    }

    public void setIdSP(int idSP) {
        this.idSP = idSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChiTietDonDH{");
        sb.append("idDDH=").append(idDDH);
        sb.append(", idSP=").append(idSP);
        sb.append(", soLuong=").append(soLuong);
        sb.append('}');
        return sb.toString();
    }
}
