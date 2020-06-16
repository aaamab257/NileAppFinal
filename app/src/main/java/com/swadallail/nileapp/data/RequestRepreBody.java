package com.swadallail.nileapp.data;

public class RequestRepreBody {
    public String fullname;
    public String mobile;
    public String identityFace ;
    public String identityBack ;
    public String driveLic ;
    public String personalImg ;

    public RequestRepreBody(String fullname, String mobile, String identityFace, String identityBack, String driveLic, String personalImg) {
        this.fullname = fullname;
        this.mobile = mobile;
        this.identityFace = identityFace;
        this.identityBack = identityBack;
        this.driveLic = driveLic;
        this.personalImg = personalImg;
    }
}
