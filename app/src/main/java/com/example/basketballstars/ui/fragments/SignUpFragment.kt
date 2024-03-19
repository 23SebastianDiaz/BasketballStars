package com.example.basketballstars.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.basketballstars.R
import com.example.basketballstars.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!! //Devuelve el _binding sin ser nu

    private val GOOGLE_SIGN_IN = 100
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        analytics()
        session()
        initListeners()
    }

    private fun analytics() {
        val analytics =
            FirebaseAnalytics.getInstance(requireContext()) //Instacia al Analytics para su integracion
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("InitScreen", bundle) //Laza en evento a firebase
    }

    private fun initListeners() {
        with(binding) {
            btnSignUp.setOnClickListener { signup() }
            btnGoogle.setOnClickListener { signUpGoogle() }
        }
    }

    private fun signup() { //Registro de usuario
        val emailSigUp = binding.etEmailSignUp.text
        val passwordSignUp = binding.etPasswordSignUp.text
        //Condicion para no introducir textos vacios
        if (emailSigUp.isNotEmpty() && passwordSignUp.isNotEmpty()) {
            FirebaseAuth.getInstance()
                //Crea el email y el password convirtiendolos a string
                .createUserWithEmailAndPassword(emailSigUp.toString(), passwordSignUp.toString())
                .addOnCompleteListener { singUp ->
                    //Notificar si fue exitosa o no el registro
                    if (singUp.isSuccessful) {
                        showHome(singUp.result?.user?.email ?: "") //envia parametros registrados
                    } else {
                        showAlert(
                            "Error de registro",
                            "Hubo un problema al registrar la cuenta."
                        ) //muestra error
                    }
                }
        }
    }

    private fun signUpGoogle() { //Registro de usuario por medio de google
        //Configuracion
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() //solicitar el email
            .build()

        //cliente autenticacion de google
        val googleClient = GoogleSignIn.getClient(requireContext(), googleConf)
        googleClient.signOut()//Asegurar que se cierre sesion en la cuenta asociada en el momento

        startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN) //Mustra vista de autenticacion de google
    }

    //Verifica si la respuesta de la activity corresponde con la autenticacion de google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //recuperar cuenta de autenticacion
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    //recupera credencial
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential) //envia la autenticacion a firebase
                        .addOnCompleteListener { signUpGoogle ->
                            if (signUpGoogle.isSuccessful){
                                showHome(account.email ?: "")
                            }else{
                                showAlert("Error de Google", "No se pudo iniciar sesión con Google. Inténtelo de nuevo.")
                            }
                        }
                }
            }catch (e: ApiException){
                showAlert("Error de google", "Se produjo un error al iniciar sesión con Google: ${e.message}")
            }
        }
    }

    //navegacion al team fragment
    private fun showHome(email: String) {
        val action = SignUpFragmentDirections.actionSignUpFragmentToTeamFragment(email)
        findNavController().navigate(action)
    }

    //Alert para manipular los posibles errores
    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Recupera los datos si existe una sesion iniciada
    private fun session() {
        val prefs = requireContext().getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )
        val email = prefs.getString("email", null)
        if (email != null) {
            showHome(email)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}