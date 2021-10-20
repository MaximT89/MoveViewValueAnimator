package com.example.testmoveanimation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    public MutableLiveData<Boolean> isGame;

    public LiveData<Boolean> getIsGame(){
        if (isGame == null){
            isGame = new MutableLiveData<>();
            loadIsGame();
        }
        return isGame;
    }

    private void loadIsGame(){
        isGame.setValue(false);
    }


}
