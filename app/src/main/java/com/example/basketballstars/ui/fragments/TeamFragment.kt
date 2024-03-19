package com.example.basketballstars.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.basketballstars.R
import com.example.basketballstars.databinding.FragmentTeamBinding
import com.google.firebase.auth.FirebaseAuth


class TeamFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!! //Devuelve el _binding sin ser nulo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = TeamFragmentArgs.fromBundle(requireArguments())
        val email = args.email

        initUI()
        setUp(email)
        saveData(email)
    }

    private fun initUI() {
        initListeners()
    }

    private fun setUp(email:String) {
        binding.tvEmail.text = email
    }

    private fun saveData(email:String) {
        val prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()
    }

    private fun initListeners() {
        binding.btnLogOut.setOnClickListener {logOut()}
    }

    private fun logOut() {
        //Se cierre la sesion y borra datos
        FirebaseAuth.getInstance().signOut()
        val prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}

