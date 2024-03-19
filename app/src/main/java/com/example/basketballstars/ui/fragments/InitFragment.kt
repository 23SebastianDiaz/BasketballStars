package com.example.basketballstars.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.basketballstars.R
import com.example.basketballstars.databinding.FragmentInitBinding


class InitFragment : Fragment() {

    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!! //Devuelve el _binding sin ser nulo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        //Navegacion al login
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_initFragment_to_loginFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}