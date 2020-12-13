package com.example.ejemploapp.fragmets


import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.text.TextUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ejemploapp.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


class LoginFragment : Fragment() {
    companion object {
        const val DEFAULT_PREFERENCES_KEY : String= "DEFAULT_PREFERENCES"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun Login() {
        var apiClient = RetrofitHelper.getInstance(getString(R.string.base_url))
        var credentials = Credentials(txtemail.text.toString(), txtpassword.text.toString())

        val getTokenRequest = apiClient.GetToken(credentials)
        getTokenRequest.enqueue(object : retrofit2.Callback<TokenInfo> {
            override fun onFailure(call: retrofit2.Call<TokenInfo>, t: Throwable) {
                Toast.makeText(activity, "ERROR" + t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(
                call: retrofit2.Call<TokenInfo>,
                response: retrofit2.Response<TokenInfo>
            ) {
                if (response.isSuccessful) {
                    activity!!.runOnUiThread {
                        run {
//                            Toast.makeText(activity, response.body()!!.jwt, Toast.LENGTH_LONG)
//                                .show()
                            Toast.makeText(activity, "Bienvenido!!", Toast.LENGTH_LONG).show()
                            stroreAccesToken(response.body()!!.jwt)

                            findNavController().navigate(R.id.mainFragment)


                        }
                    }
                }
            }

        })
    }



    fun stroreAccesToken( token:String){
        val sharedPreferences= this.requireActivity().getSharedPreferences("DEFAULT_PREFERENCES", Context.MODE_PRIVATE).edit()
        sharedPreferences.putString(getString(R.string.access_token),token)
        sharedPreferences.commit()
    }





    fun validar(email:String, contraseña:String) :Boolean{
        return when{
            TextUtils.isEmpty(email)->{
                txtemail.error="No deje campos vacios"
                 false
            }
            TextUtils.isEmpty(contraseña)->{
                txtpassword.error="No deje campos vacios"

                false
            }
            else -> true
        }

    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //
        val btn:View  = inflater.inflate(R.layout.fragment_login, container, false)
        getActivity()?.setTitle("Inicio de sesión");

        btn.btnlogin.setOnClickListener(){
            if (validar(txtemail.text.toString(),txtpassword.text.toString())){
                Login()

            }

        }
        return btn

    }


}





