package com.example.basketballstars.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.basketballstars.R
import com.example.basketballstars.databinding.FragmentLoginBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth



class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!! //Devuelve el _binding sin ser nulo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        analytics()
        initListeners()
        session()
    }

    private fun analytics() {
        val analytics = FirebaseAnalytics.getInstance(requireContext()) //Instacia al Analytics para su integracion
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("InitScreen", bundle) //Laza en evento a firebase
    }

    private fun initListeners() {
        with(binding){
            tvSignUp.setOnClickListener {navigateSignUp()}
            btnLogin.setOnClickListener {login() }
        }
    }

    private fun navigateSignUp() = findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)

    private fun login() {
        val emailLogin = binding.etEmailLogin.text.toString()
        val passwordlogin = binding.etPasswordLogin.text.toString()
        val username = emailLogin.substringBefore("@")


        //Condicion para no introducir textos vacios
        if (emailLogin.isNotEmpty() && passwordlogin.isNotEmpty()) {
            FirebaseAuth.getInstance()
                //Crea el email y el password convirtiendolos a string
                .signInWithEmailAndPassword(emailLogin, passwordlogin)
                .addOnCompleteListener { login->
                    //Notificar si fue exitosa o no el ingreso
                    if (login.isSuccessful){
                        showHome(login.result?.user?.email ?: "", username  ) //envia parametros registrados
                    }else{
                        showAlert("Error de Login","No se pudo iniciar sesión. Verifique las credenciales e inténtelo de nuevo") //muestra error
                    }
                }
        }
    }

    private fun showHome(email:String, username:String){
        val action = LoginFragmentDirections.actionLoginFragmentToTeamFragment(email, username)
        findNavController().navigate(action)
    }

    //Alert para manipular los posibles errores
    private fun showAlert(title:String, message:String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun session() {
        val prefs = requireContext().getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )
        val email = prefs.getString("email", null)
        val username = prefs.getString("username", null)
        if (email != null && username != null) {
            showHome(email, username)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}