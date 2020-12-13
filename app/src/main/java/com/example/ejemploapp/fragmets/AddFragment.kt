package com.example.ejemploapp.fragmets

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.ejemploapp.Product
import com.example.ejemploapp.ProductsService
import com.example.ejemploapp.R
import com.example.ejemploapp.RetrofitHelper
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFragment : Fragment() {

    var productsService : ProductsService?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            findNavController().navigate(R.id.mainFragment)

        }




    }
    fun Add(){
        val name= txtnombre.text.toString()
        val modelo= txtmodelo.text.toString()
        val precio= txtPrecio.text.toString()
        val descripcion= txtdecripcion.text.toString()


        val accesToken :String =getAccessToken()!!
        productsService= RetrofitHelper.getAunthenticatedInstance(getString(R.string.base_url),accesToken)
        productsService!!.addProduct(name,modelo,precio.toFloat(),descripcion).enqueue(object : Callback<Product> {
            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(activity, "Hay un error en los campos, por favor verifique", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                Toast.makeText(activity, "Ha sido guardado correctamente", Toast.LENGTH_LONG).show()


                findNavController().navigate(R.id.mainFragment)

            }

        })
    }
    fun validar(name:String, modelo:String,precio:String,descripcion:String) :Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                txtnombre.error="No deje campos vacios"
                false
            }
            TextUtils.isEmpty(modelo)->{
                txtmodelo.error="No deje campos vacios"

                false
            }
            TextUtils.isEmpty(precio.toString())->{
                txtPrecio.error="No deje campos vacios"

                false
            }
            TextUtils.isEmpty(descripcion)->{
                txtdecripcion.error="No deje campos vacios"

                false
            }
            else -> true
        }

    }

    private fun getAccessToken():String? {
        return requireActivity().getSharedPreferences(LoginFragment.DEFAULT_PREFERENCES_KEY, Context.MODE_PRIVATE).getString(getString(R.string.access_token),"")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val btn:View  = inflater.inflate(R.layout.fragment_add, container, false)
        getActivity()?.setTitle("Agregar elemento");

        btn.btnAdd.setOnClickListener(){
            if(validar(txtnombre.text.toString(),txtmodelo.text.toString(),txtPrecio.text.toString(),txtdecripcion.text.toString())){
                Add()
            }

        }
        return btn

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {

            }
    }
}