package com.example.apptest2019.interfaces
//интерфейс помогающий в настройке действия по нажатию кнопки Back у каждого фрагманта
interface FragmentBackButtonPressed {
    fun onBackPressed(): Boolean
}