package com.keika.thunghiem01.model;

import java.io.Serializable;

/**
 * Created by Son-Auto on 4/6/2018.
 */

public class SanPham implements Serializable {
    private int idSP;
    private String tenSP;
    private String giaSP;
    private int soLuongTon;

    public SanPham() {
    }

    public SanPham(int idSP, String tenSP, String giaSP, int soLuongTon) {
        this.idSP = idSP;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.soLuongTon = soLuongTon;
    }

    public int getIdSP() {
        return idSP;
    }

    public void setIdSP(int idSP) {
        this.idSP = idSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(String giaSP) {
        this.giaSP = giaSP;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SanPham{");
        sb.append("idSP=").append(idSP);
        sb.append(", tenSP='").append(tenSP).append('\'');
        sb.append(", giaSP='").append(giaSP).append('\'');
        sb.append(", soLuongTon=").append(soLuongTon);
        sb.append('}');
        return sb.toString();
    }
}
