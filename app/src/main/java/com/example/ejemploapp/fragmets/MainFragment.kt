package com.example.ejemploapp.fragmets

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ejemploapp.*
import com.example.ejemploapp.fragmets.LoginFragment.Companion.DEFAULT_PREFERENCES_KEY
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.list_row_main.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {
    var productsService : ProductsService?=null
    private lateinit var adapter:MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        val accesToken :String =getAccessToken()!!
        productsService= RetrofitHelper.getAunthenticatedInstance(getString(R.string.base_url),accesToken)
        productsService!!.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(activity, "Ta vacio bebe", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                //val album = ArrayList<Product>()
               // album.add(Product(200000,"queso"))
                adapter.setListData(response.body()!!.toList() as ArrayList<Product>)
//                Toast.makeText(activity, "${response.body()}", Toast.LENGTH_LONG).show()

            }

        })




    }

    private fun getAccessToken():String? {
        return requireActivity().getSharedPreferences(DEFAULT_PREFERENCES_KEY, Context.MODE_PRIVATE).getString(getString(R.string.access_token),"")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val vista:View =inflater.inflate(R.layout.fragment_main, container, false)
        getActivity()?.setTitle("Productos MAX");


        adapter= MainAdapter(this!!.requireActivity())

        vista.recyclerView.layoutManager = LinearLayoutManager(this!!.requireActivity())
        vista.recyclerView.adapter= adapter




        vista.btnadd.setOnClickListener(){
            findNavController().navigate(R.id.addFragment)

        }



        return vista
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}