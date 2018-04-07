package com.keika.thunghiem01.model;

/**
 * Created by Son-Auto on 4/6/2018.
 */

public class CuaHang {
    private int idCH;
    private String tenCH;
    private String diaChi;
    private String nguoiLH;
    private String soDT;

    public CuaHang() {
    }

    public CuaHang(int idCH, String tenCH, String diaChi, String nguoiLH, String soDT) {
        this.idCH = idCH;
        this.tenCH = tenCH;
        this.diaChi = diaChi;
        this.nguoiLH = nguoiLH;
        this.soDT = soDT;
    }

    public int getIdCH() {
        return idCH;
    }

    public void setIdCH(int idCH) {
        this.idCH = idCH;
    }

    public String getTenCH() {
        return tenCH;
    }

    public void setTenCH(String tenCH) {
        this.tenCH = tenCH;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNguoiLH() {
        return nguoiLH;
    }

    public void setNguoiLH(String nguoiLH) {
        this.nguoiLH = nguoiLH;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CuaHang{");
        sb.append("idCH=").append(idCH);
        sb.append(", tenCH='").append(tenCH).append('\'');
        sb.append(", diaChi='").append(diaChi).append('\'');
        sb.append(", nguoiLH='").append(nguoiLH).append('\'');
        sb.append(", soDT='").append(soDT).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
