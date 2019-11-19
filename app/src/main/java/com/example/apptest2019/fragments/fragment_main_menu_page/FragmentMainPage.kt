package com.example.apptest2019.fragments.fragment_main_menu_page

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.apptest2019.App
import com.example.apptest2019.R
import com.example.apptest2019.activity.ActionBarHandlerInterface
import com.example.apptest2019.activity.DrawerLockerHandlerInterface
import com.example.apptest2019.databinding.FragmentMainPageBinding
import com.example.apptest2019.fragments.fragment_login_page.FragmentLoginPageDirections
import com.example.apptest2019.repository.database.database_models.UserDataModel

class FragmentMainPage : Fragment() {

    private lateinit var viewModel: ViewModelFragmentMainPage

    private lateinit var viewModelFactory: ViewModelFragmentMainPage.ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMainPageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_page, container, false)
        val application = this.activity!!.application

        // блок управляет системными элементами через активити /////////////
        (activity as DrawerLockerHandlerInterface).unlockDrawerMenu()    ///
        (activity as ActionBarHandlerInterface).setTitle("SmartList")    ///
        ////////////////////////////////////////////////////////////////////

        // Вставляет во ViewModel при создании аргумент через фабрику ////////////////////
        viewModelFactory = ViewModelFragmentMainPage.ViewModelFactory(                 ///
            application                                                                ///
        )                                                                              ///
        viewModel = ViewModelProviders.of(this, viewModelFactory)             ///
            .get(ViewModelFragmentMainPage::class.java)                                ///
        //////////////////////////////////////////////////////////////////////////////////


        // инициализирует адаптер и конфигурирует его //////////////////////////////////////////////////////////////////////////////////
        val adapter = RecyclerMainPageUsersAdapter(                                                                                  ///
            object : BasicAdapterCardClickListener {                                                                                 ///
                override fun cardClicked(f: UserDataModel) {                                                                         ///
                    Toast.makeText(context,"Началось удаление ${f.firstName +" "+ f.lastName}. ",Toast.LENGTH_SHORT).show()                ///
                }                                                                                                                    ///
            }, object : BasicAdapterHeaderCardClickListener {                                                                        ///
                override fun cardHeaderClicked(typeOfButton: Int) {                                                                  ///
                    viewModel.addUser()                                                                                              ///
                    Toast.makeText(context,"Началось добавление $typeOfButton ",Toast.LENGTH_SHORT).show()                                   ///
                }                                                                                                                    ///
            })                                                                                                                       ///
        /// вешает обозреватель на recycleview.adaptor.items теперь они привязвны к viewmodel                                        ///
        viewModel.userList.observe(viewLifecycleOwner, Observer {                                                                    ///
            it?.let {                                                                                                                ///
                adapter.items =                                                                                                      ///
                    it                                                                                                               ///
            }                                                                                                                        ///
        })                                                                                                                           ///
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        viewModel.viewModelAdaptor = adapter
        binding.viewModel = viewModel
        binding.lifecycleOwner = this //подписывает livedata
        setHasOptionsMenu(true)//включает меню (3 точки (дополнительное меню) ) в navBar

        return binding.root
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        //при неверифицированом домтупе кидает юзера на Loginpage
//        if (App.isTrustedUser.value == false) {
//            view?.findNavController()?.navigate(
//                //относит на страницу логинв по истечении таймера "сертификата"
//                FragmentLoginPageDirections.actionGlobalFragmentLoginPage()
//            )
//        }
//    }



    // инициализирует меню из ресурсов к фрагменту////////////////////////////////
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {     ///
        super.onCreateOptionsMenu(menu, inflater)                              ///
        inflater.inflate(R.menu.menu2, menu)                                   ///
    }//(3 точки (дополнительное меню) )                                        ///

    // настраивает меню для перехода на ссылки id в навигаторе navigation_base ///
    // id menu item должен быть равен id <fragment> в navigation_base          ///
    override fun onOptionsItemSelected(item: MenuItem): Boolean {              ///
        return NavigationUI.onNavDestinationSelected(                          ///
            item,                                                              ///
            view!!.findNavController()                                         ///
        ) || super.onOptionsItemSelected(item)                                 ///
    }/////////////////////////////////////////////////////////////////////////////


}