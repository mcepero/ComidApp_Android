package com.example.manuelcepero.comidapp.viewmodels;

import android.view.View;

import com.example.manuelcepero.comidapp.models.Usuario;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> Username = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<Usuario> userMutableLiveData;

    public MutableLiveData<Usuario> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {

        Usuario loginUser = new Usuario(Username.getValue(), Password.getValue());

        userMutableLiveData.setValue(loginUser);

    }


}
