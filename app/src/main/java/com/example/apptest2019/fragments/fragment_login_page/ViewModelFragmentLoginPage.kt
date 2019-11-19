package com.example.apptest2019.fragments.fragment_login_page

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.apptest2019.R
import com.example.apptest2019.common.sha256

class ViewModelFragmentLoginPage(
    application: Application
) : AndroidViewModel(application) {

    private val context: Context = application

    private var prefs: SharedPreferences? = null

    var navigatorListener: LoginPageNavigationListener? =
        null // используется для обратного вызова функций навигатора

    var handler: LoginPageHandlerListener? = null // обработчик событий вызывающий колбек фрагмента

    private val _buttonResetBlockTrig = MutableLiveData<Boolean>()               //
    val buttonResetBlockTrig: LiveData<Boolean> get() = _buttonResetBlockTrig    //блокирует кнопку резет!

    private val _textViewLogin = MutableLiveData<String>()        //
    val textViewLogin: LiveData<String> get() = _textViewLogin    //переменная хранит ЛОГИН димамически изменяемый
    fun setLoginValue (login:String){
        _textViewLogin.value = login
    }

    private val _textViewPass = MutableLiveData<String>()       //
    val textViewPass: LiveData<String> get() = _textViewPass    //Переменная хранит ПАРОЛЬ динамически изменяемый
    fun setPassValue (pass:String){
        _textViewPass.value = pass
    }

    private var _CurrentLogin: String? =
        null     //      переменная хранит ЛОГИН который был ранее СОХРАНЕН!

    private var _CurrentPass: String? =
        null      //      Переменная хранит ПАРОЛЬ который был ранее СОХРАНЕН!

    init {
        // сбрасывает тригер резета в ноль при инициализации модели
        _buttonResetBlockTrig.value =
            true

        //подучаем переменную SharedPreferences по ключу
        prefs = context.getSharedPreferences(
            context.resources.getString(R.string.user_pass_and_login_prefs_name),
            Context.MODE_PRIVATE
        )

        //получаем нужные данные по ключу
        _CurrentLogin = prefs?.getString(context.resources.getString(R.string.user_login), null)
        _textViewLogin.value = _CurrentLogin
        _CurrentPass = prefs?.getString(context.resources.getString(R.string.user_pass), null)

        if ((_CurrentLogin == null) or (_CurrentPass == null)) {
            _buttonResetBlockTrig.value = false
        }
    }

    //функция выполняет вход (логин и пароль выполнены в LiveData связанной с фрагментами)
    fun loginInit() {
        val regexLogin = "^((8|\\+7)[\\- ]?)?(\\(?9\\d{2}\\)?[\\- ]?)?[\\d\\- ]{7}$".toRegex()
        val regexPass = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}".toRegex()

        val currentThisLogin = regexLogin.matches(_textViewLogin.value.toString())
        val currentThisPass = regexPass.matches(_textViewPass.value.toString())

        if (!currentThisLogin) {
            handler?.showToastByType(ToastInLoginPanel.WRONG_LOGIN)
            _buttonResetBlockTrig.value = true
            return
        }

        if (!currentThisPass) {
            handler?.showToastByType(ToastInLoginPanel.WRONG_PASS)
            _buttonResetBlockTrig.value = true
            return
        }

        //  1)если ранее не было сохранения
        if ((_CurrentLogin == null) or (_CurrentPass == null)) {
            prefs?.edit()
                ?.putString(context.resources.getString(R.string.user_login), _textViewLogin.value)
                ?.apply()
            prefs?.edit()
                ?.putString(context.resources.getString(R.string.user_pass), _textViewPass.value?.sha256())
                ?.apply()
            _CurrentLogin =  _textViewLogin.value
            _CurrentPass = _textViewPass.value?.sha256()
            navigatorListener?.moveToMainPage()
            return
        }
        // 2)если ранее были сохранения
        if ((_CurrentLogin == _textViewLogin.value) and (_CurrentPass == _textViewPass.value?.sha256())) {
            navigatorListener?.moveToMainPage()
        } else {
            handler?.showToastByType(ToastInLoginPanel.WRONG_LOGIN_OR_PASS)
        }
    }

    fun btnReset() {
        _buttonResetBlockTrig.value = false
        prefs?.edit()
            ?.putString(context.resources.getString(R.string.user_login), null)
            ?.apply()
        prefs?.edit()
            ?.putString(context.resources.getString(R.string.user_pass),null)
            ?.apply()
        _CurrentLogin = null
        _CurrentPass = null
        handler?.showToastByType(ToastInLoginPanel.RESET_WAS_DONE)
    }

    override fun onCleared() {
        super.onCleared()
        navigatorListener = null
        handler = null
    }

    // определяет интерфейс для местной навигации в текущем фрагменте
    interface LoginPageNavigationListener {
        fun moveToMainPage()
    }

    //определяет интерфейс для остальных операций помимо навигации
    interface LoginPageHandlerListener {
        fun showToastByType(typeOfToast: ToastInLoginPanel) //всплывающие окна Toast во фрагменте
    }

    enum class ToastInLoginPanel {
        WRONG_LOGIN, WRONG_PASS, RESET_WAS_DONE, ENTRY_WAS_NOT_FOUND, ENTRY_WAS_FOUND, WRONG_LOGIN_OR_PASS
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ViewModelFragmentLoginPage::class.java)) {
                return ViewModelFragmentLoginPage(
                     application
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModelFragmentThird class")
        }
    }
}