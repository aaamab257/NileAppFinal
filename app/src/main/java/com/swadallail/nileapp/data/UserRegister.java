package com.swadallail.nileapp.data;

public class UserRegister {
    private String email ;
    private String password ;
    private String confirmPassword ;
    private String userType ;

    public UserRegister(String email, String password, String confirmPassword, String userType) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.userType = userType;
    }
}
