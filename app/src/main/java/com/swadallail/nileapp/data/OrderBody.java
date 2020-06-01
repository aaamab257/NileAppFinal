package com.swadallail.nileapp.data;

public class OrderBody {
    private String img  ;
    private String description ;
    private String fromLat ;
    private String fromLng ;
    private String toLat ;
    private String toLng ;
    private int hours ;
    private String fromAddress  ;
    private String toAddress ;

    public OrderBody(String img, String description, String fromLat, String fromLng, String toLat, String toLng, int hours, String fromAddress, String toAddress) {
        this.img = img;
        this.description = description;
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.toLat = toLat;
        this.toLng = toLng;
        this.hours = hours;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
    }
}
