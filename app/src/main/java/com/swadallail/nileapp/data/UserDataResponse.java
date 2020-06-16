package com.swadallail.nileapp.data;

public class UserDataResponse {
    private String username;
    private String fullName;
    private String pic;
    private String role;
    private Boolean phoneConfirmed;
    private Boolean mailConfirmed;

    public Boolean getPhoneConfirmed() {
        return phoneConfirmed;
    }

    public void setPhoneConfirmed(Boolean phoneConfirmed) {
        this.phoneConfirmed = phoneConfirmed;
    }

    public Boolean getMailConfirmed() {
        return mailConfirmed;
    }

    public void setMailConfirmed(Boolean mailConfirmed) {
        this.mailConfirmed = mailConfirmed;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
