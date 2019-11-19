package com.example.apptest2019.fragments.fragment_main_menu_page

import android.app.Application
import androidx.lifecycle.*
import com.example.apptest2019.repository.Repository
import kotlinx.coroutines.*

class ViewModelFragmentMainPage(
    application: Application
) : AndroidViewModel(application) {

    //    private val _userLiveData = MutableLiveData<List<UserDataModel>>()
    //    val userLiveData: LiveData<List<UserDataModel>> get() = _userLiveData
    private val context = application.applicationContext!!
    private val repository = Repository.getInstance(context)

    //создает и конфигурирует корутину
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    //забирает из репозитория данные по добавлению в бд новых данных UserDataModel
    val userList = repository.userLiveData
    // создает адаптор для его динамического обновления во время перезапуска фрагмента
    var viewModelAdaptor: RecyclerMainPageUsersAdapter? = null

    fun addUser() {
        coroutineScope.launch {
            repository.addNewUser()
        }
    }

    fun deleteUser() {
        coroutineScope.launch {
            repository.deleteAllUsers()
        }
    }

//    fun populateList() {
//
//        coroutineScope.launch {
//            val items = getSomeUserList()
//            withContext(Dispatchers.Main) {
//                showSumeUserList(items)
//            }
//        }
//    }

//    suspend fun getSomeUserList(): MutableList<UserDataModel> {
//        val api = RandomUserApi.getInstance()
//
//        val list = mutableListOf<UserDataModel>()
//        try {
//
//            var listResult = api.getSomeUserAsync().await()
//            list.add(UserDataModel(firstName = listResult.results[0].name.first,lastName = listResult.results[0].name.last))
//        } catch (e: Exception) {
//        }
//        return list
//    }
//
//    suspend fun showSumeUserList(items: MutableList<UserDataModel>) {
//
//
//        _userLiveData.value = items
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ViewModelFragmentMainPage::class.java)) {
                return ViewModelFragmentMainPage(
                    application
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModelFragmentMainPage class")
        }
    }

}
