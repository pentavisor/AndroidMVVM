package com.example.apptest2019.fragments.fragment_secret_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.apptest2019.R
import com.example.apptest2019.databinding.FragmentMenuBaseBinding

class FragmentSecretMenu: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMenuBaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_menu_base, container, false)
        binding.textViewMenu.text = "MENU MENU"
        return binding.root
    }
}