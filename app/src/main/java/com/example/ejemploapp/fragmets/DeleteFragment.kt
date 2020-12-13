package com.example.ejemploapp.fragmets

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ejemploapp.Product
import com.example.ejemploapp.ProductsService
import com.example.ejemploapp.R
import com.example.ejemploapp.RetrofitHelper
import kotlinx.android.synthetic.main.fragment_delete.view.*
import kotlinx.android.synthetic.main.fragment_edit.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeleteFragment : Fragment() {
    var productsService : ProductsService?=null
    private val args:DeleteFragmentArgs by navArgs()
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            findNavController().navigate(R.id.mainFragment)

        }





    }
    fun ShowDialog(id :Int){
        AlertDialog.Builder(this.activity)
            .setTitle("Está seguro que desea eliminar este elemento?")
            .setPositiveButton("Si",
                DialogInterface.OnClickListener { dialog, which ->
                    Delete(id)
                    findNavController().navigate(R.id.mainFragment)
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which ->
                    findNavController().navigate(R.id.mainFragment)

                })
            .show()
    }
    fun Delete(id :Int) {


        val accesToken :String =getAccessToken()!!
        productsService = RetrofitHelper.getAunthenticatedInstance(getString(R.string.base_url), accesToken)
        productsService!!.delete(id)
            .enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(activity, "Error al intentar eliminar el elemento,intentelo mas tarde", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Toast.makeText(activity, "Ha sido borrado correctamente", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.mainFragment)


                }

            })
    }
    private fun getAccessToken():String? {
        return requireActivity().getSharedPreferences(LoginFragment.DEFAULT_PREFERENCES_KEY, Context.MODE_PRIVATE).getString(getString(R.string.access_token),"")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista:View = inflater.inflate(R.layout.fragment_delete, container, false)
        getActivity()?.setTitle("Eliminar elemento");

        product=args.productoEdit
        val id=Integer.toString(product.id)

        vista.txtnombreD.setText(product.name)
        vista.txtmodeloD.setText(product.model)
        vista.txtPrecioD.setText(product.price.toString())
        vista.txtdecripcionD.setText(product.description)
        vista.btnDelete.setOnClickListener(){
            ShowDialog(id.toInt())

        }
        return vista
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DeleteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}