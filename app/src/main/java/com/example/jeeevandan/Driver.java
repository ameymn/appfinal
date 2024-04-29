package com.example.jeeevandan;

import java.util.List;

public class Driver {
    private List<TripDetails> list;

    public Driver(List<TripDetails> list) {
        this.list = list;
    }

    public List<TripDetails> getList() {
        return list;
    }

    public void setList(List<TripDetails> list) {
        this.list = list;
    }

    public static class TripDetails {
        private double d_lat;
        private double d_lngt;
        private double r_lat;
        private double r_lngt;
        private int t_id;
        private String driver;
        private boolean trans_end;

        public double getD_lat() {
            return d_lat;
        }

        public void setD_lat(double d_lat) {
            this.d_lat = d_lat;
        }

        public double getD_lngt() {
            return d_lngt;
        }

        public void setD_lngt(double d_lngt) {
            this.d_lngt = d_lngt;
        }

        public double getR_lat() {
            return r_lat;
        }

        public void setR_lat(double r_lat) {
            this.r_lat = r_lat;
        }

        public double getR_lngt() {
            return r_lngt;
        }

        public void setR_lngt(double r_lngt) {
            this.r_lngt = r_lngt;
        }

        public int getT_id() {
            return t_id;
        }

        public void setT_id(int t_id) {
            this.t_id = t_id;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public boolean isTrans_end() {
            return trans_end;
        }

        public void setTrans_end(boolean trans_end) {
            this.trans_end = trans_end;
        }
    }
}