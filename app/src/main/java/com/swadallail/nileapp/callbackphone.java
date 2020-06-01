package com.swadallail.nileapp;

import com.google.firebase.auth.PhoneAuthProvider;

public class callbackphone {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    PhoneAuthProvider.ForceResendingToken Token;


    public void setForceResendingToken(PhoneAuthProvider.ForceResendingToken Token) {
        this.Token = Token;
    }

    public PhoneAuthProvider.ForceResendingToken getForceResendingToken() {
        return Token;
    }


    public PhoneAuthProvider.OnVerificationStateChangedCallbacks getmCallBacks() {
        return mCallBacks;
    }

    public void setmCallBacks(PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks) {
        this.mCallBacks = mCallBacks;
    }


}
