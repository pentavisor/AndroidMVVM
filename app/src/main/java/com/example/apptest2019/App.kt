package com.example.apptest2019

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class App : Application() {


    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        //Trust user  control timer ///////////////////////////////////////////////////////////////////////
        private val COUNT_DOWN_TIME = 60_000L                                                           ///
        private val ONE_SECOND = 1_000L                                                                 ///
        private val _isTrustedUser = MutableLiveData<Boolean>().apply { value = false }                 ///
        val isTrustedUser: LiveData<Boolean> get() = _isTrustedUser    //проверка сессии поьзоватедя    ///
        // проверка сессии польщователя каждые COUNT_DOWN_TIME милисекунд через ONE_SECOND милисекунд   ///
        private val checkLoginTimer = object : CountDownTimer(COUNT_DOWN_TIME, ONE_SECOND) {            ///
            override fun onFinish() {                                                                   ///
                _isTrustedUser.value = false                                                            ///
            }                                                                                           ///
            override fun onTick(millisUntilFinished: Long) {                                            ///
            }                                                                                           ///
        }                                                                                               ///
        // установить пользователя как прошедшего проверку, старт отсчета времени сессии                ///
        fun setUserAsTrusted() {                                                                        ///
            _isTrustedUser.value = true                                                                 ///
            checkLoginTimer.start()                                                                     ///
        }                                                                                               ///
        ///////////////////////////////////////////////////////////////////////////////////////////////////
    }

}