package com.example.basketballstars.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.basketballstars.R
import com.example.basketballstars.data.HeaderInterceptor
import com.example.basketballstars.databinding.FragmentTeamBinding
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TeamFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!! //Devuelve el _binding sin ser nulo

    private lateinit var retrofit: Retrofit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = TeamFragmentArgs.fromBundle(requireArguments())
        val email = args.email
        val username = args.username

        retrofit = getRetrofit()
        initUI()
        setUp(username)
        saveData(email, username)
    }

    private fun initUI() {
        initListeners()
    }
    @SuppressLint("SetTextI18n")
    private fun setUp(username: String) {
        binding.tvUserName.text = "Welcome, $username!"
    }
    private fun saveData(email: String, username: String) {
        val prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("username", username)
        prefs.apply()
    }
    private fun initListeners() {
        binding.btnLogOut.setOnClickListener {logOut()}
    }
    private fun logOut() {
        val prefs = requireContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        AlertDialog.Builder(requireContext())
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                prefs.clear()
                prefs.apply()

                // Se cierra la sesión y borra datos
                FirebaseAuth.getInstance().signOut()
                onBackPressed()

            }
            .setNegativeButton("No", null)
            .show()
    }
    private fun onBackPressed() {
        // Verificar si estamos en el TeamFragment
        if (isAdded) { // Asegurar que el fragmento esté añadido a la actividad
            requireActivity().finish() // Cerrar la actividad
        }
    }

    //Se cronstruye retrofit
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api-nba-v1.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    //Se crea cliente OkHttpClient (solicitudes Htttp)
    private fun getClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}



