package com.swadallail.nileapp.api.model;

public class Cities {
    private int cityId;
    private String cityName;
    private double lat;
    private double lng;
    /*
        @SerializedName("cityId")
        @Expose
        private Integer cityId;

        */

    public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

    public double getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }


    public double getLng() {
        return lng;
    }

    public void setLng(int lng) {
        this.lng = lng;
    }


}