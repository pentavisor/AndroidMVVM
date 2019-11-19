package com.example.apptest2019.fragments.fragment_login_page

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.apptest2019.App
import com.example.apptest2019.R
import com.example.apptest2019.activity.DrawerLockerHandlerInterface
import com.example.apptest2019.databinding.FragmentLoginPageBinding
import com.example.apptest2019.interfaces.FragmentBackButtonPressed
import kotlin.system.exitProcess


class FragmentLoginPage : Fragment(), FragmentBackButtonPressed {

    private lateinit var viewModel: ViewModelFragmentLoginPage

    private lateinit var viewModelFactory: ViewModelFragmentLoginPage.ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginPageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_page, container, false)
        val application = this.activity!!.application


        // блок управляет системными элементами через активити /////////////
        (activity as DrawerLockerHandlerInterface).lockDrawerMenu()      ///
        ////////////////////////////////////////////////////////////////////

        //Вставляет во ViewModel при создании аргумент через фабрику//////////////////////
        viewModelFactory = ViewModelFragmentLoginPage.ViewModelFactory(                ///
            application                                                                ///
        )                                                                              ///
        viewModel = ViewModelProviders.of(this, viewModelFactory)             ///
            .get(ViewModelFragmentLoginPage::class.java)                               ///
        //////////////////////////////////////////////////////////////////////////////////

        viewModel.handler =
            object : ViewModelFragmentLoginPage.LoginPageHandlerListener {
                override fun showToastByType(typeOfToast: ViewModelFragmentLoginPage.ToastInLoginPanel) {
                    showToast(typeOfToast)
                }
            }
        viewModel.navigatorListener =
            object : ViewModelFragmentLoginPage.LoginPageNavigationListener {
                override fun moveToMainPage() {
                    App.setUserAsTrusted()
                    view?.findNavController()?.navigate(
                        FragmentLoginPageDirections.actionFragmentLoginPageToFragmentMainPage()
                    )
                }
            }
        binding.Password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPassValue(s.toString())
            }
        })
        binding.Login.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setLoginValue(s.toString())
            }
        })

        // эта запись + в layout "@{viewModel.text}" - заменяет запись ниже////
        binding.lifecycleOwner = this                                       ///
        //вешает обозреватель поля textView2 из viewModel фрагмента         ///
        //viewModel.score.observe(this, Observer {                          ///
        //   binding.textView2.text = viewModel.score.value.toString()      ///
        //})///////////////////////////////////////////////////////////////////
        binding.viewModel = viewModel
        return binding.root
    }

    fun showToast(type: ViewModelFragmentLoginPage.ToastInLoginPanel) {
        var resId: String? = null
        when (type) {
            ViewModelFragmentLoginPage.ToastInLoginPanel.WRONG_LOGIN -> {
                resId = resources.getString(R.string.wron_login)
            }
            ViewModelFragmentLoginPage.ToastInLoginPanel.WRONG_PASS -> {
                resId = resources.getString(R.string.wron_pass)
            }
            ViewModelFragmentLoginPage.ToastInLoginPanel.RESET_WAS_DONE -> {
                resId = resources.getString(R.string.reset_was_done)
            }
            ViewModelFragmentLoginPage.ToastInLoginPanel.ENTRY_WAS_NOT_FOUND -> {
                resId = resources.getString(R.string.entry_was_not_found)
            }
            ViewModelFragmentLoginPage.ToastInLoginPanel.ENTRY_WAS_FOUND -> {
                resId = resources.getString(R.string.entry_was_found)
            }
            ViewModelFragmentLoginPage.ToastInLoginPanel.WRONG_LOGIN_OR_PASS -> {
                resId = resources.getString(R.string.wrong_login_or_pass)
            }
        }

        val toast = Toast.makeText(
            activity!!,
            resId,
            Toast.LENGTH_SHORT
        )
        toast.show()

    }

    override fun onBackPressed(): Boolean {
        activity?.finish()
        exitProcess(0)
    }


}
