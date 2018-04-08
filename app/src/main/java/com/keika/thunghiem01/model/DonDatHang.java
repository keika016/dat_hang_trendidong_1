package com.keika.thunghiem01.model;

import java.io.Serializable;

/**
 * Created by Son-Auto on 4/6/2018.
 */

public class DonDatHang implements Serializable {
    private int idDDH;
    private String giaTriDDH;
    private String ngayVieng;
    private int idCH;

    public DonDatHang() {
    }

    public DonDatHang(int idDDH, String giaTriDDH, String ngayVieng, int idCH) {
        this.idDDH = idDDH;
        this.giaTriDDH = giaTriDDH;
        this.ngayVieng = ngayVieng;
        this.idCH = idCH;
    }
    public DonDatHang(int idCH,int idDDH, String giaTriDDH) {
        this.idDDH = idDDH;
        this.giaTriDDH = giaTriDDH;
        this.idCH = idCH;
    }

    public int getIdDDH() {
        return idDDH;
    }

    public void setIdDDH(int idDDH) {
        this.idDDH = idDDH;
    }

    public String getGiaTriDDH() {
        return giaTriDDH;
    }

    public void setGiaTriDDH(String giaTriDDH) {
        this.giaTriDDH = giaTriDDH;
    }

    public String getNgayVieng() {
        return ngayVieng;
    }

    public void setNgayVieng(String ngayVieng) {
        this.ngayVieng = ngayVieng;
    }

    public int getIdCH() {
        return idCH;
    }

    public void setIdCH(int idCH) {
        this.idCH = idCH;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DonDatHang{");
        sb.append("idDDH=").append(idDDH);
        sb.append(", giaTriDDH='").append(giaTriDDH).append('\'');
        sb.append(", ngayVieng='").append(ngayVieng).append('\'');
        sb.append(", idCH=").append(idCH);
        sb.append('}');
        return sb.toString();
    }
}
