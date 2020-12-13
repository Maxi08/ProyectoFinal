package com.example.ejemploapp.fragmets

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ejemploapp.Product
import com.example.ejemploapp.ProductsService
import com.example.ejemploapp.R
import com.example.ejemploapp.RetrofitHelper
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_edit.view.*
import kotlinx.android.synthetic.main.list_row_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue


class EditFragment : Fragment() {
    var productsService : ProductsService?=null
    private val args:EditFragmentArgs by navArgs()
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            findNavController().navigate(R.id.mainFragment)

        }

    }
    fun Update(id:Int) {

        val name = txtnombreU.text.toString()
        val modelo = txtmodeloU.text.toString()
        val precio = txtPrecioU.text.toString()
        val descripcion = txtdecripcionU.text.toString()
        val objeto = Product(id, name, modelo, precio.toFloat(), descripcion)

        val accesToken :String =getAccessToken()!!
        productsService = RetrofitHelper.getAunthenticatedInstance(getString(R.string.base_url), accesToken)
        productsService!!.updateProduct(id,objeto)
            .enqueue(object : Callback<Product> {
                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Toast.makeText(activity, "Ha ocurrido un erro,por favor verifique los campos", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    Toast.makeText(activity, "Ha sido actualizado correctamente", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.mainFragment)


                }

            })
    }
    fun validar(name:String, modelo:String,precio:String,descripcion:String) :Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                txtnombreU.error="No deje campos vacios"
                false
            }
            TextUtils.isEmpty(modelo)->{
                txtmodeloU.error="No deje campos vacios"

                false
            }
            TextUtils.isEmpty(precio)->{
                txtPrecioU.error="No deje campos vacios"

                false
            }
            TextUtils.isEmpty(descripcion)->{
                txtdecripcionU.error="No deje campos vacios"

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
        // Inflate the layout for this fragment
        val vista:View = inflater.inflate(R.layout.fragment_edit, container, false)
        getActivity()?.setTitle("Actualizar elemento");

        product=args.product
        val id=Integer.toString(product.id)



        vista.txtnombreU.setText(product.name)
        vista.txtmodeloU.setText(product.model)
        vista.txtPrecioU.setText(product.price.toString())
        vista.txtdecripcionU.setText(product.description)



        vista.btnUpdate.setOnClickListener(){
            if(validar(txtnombreU.text.toString(),txtmodeloU.text.toString(),txtPrecioU.text.toString(),txtdecripcionU.text.toString())){
                    Update(id.toInt())
            }
        }


        return vista

    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}