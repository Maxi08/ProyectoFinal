package com.example.ejemploapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ejemploapp.fragmets.MainFragmentDirections
import kotlinx.android.synthetic.main.list_row_main.view.*


class MainAdapter(private val context: Context) :

    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var dataList = ArrayList<Product>()


    fun setListData(data: ArrayList<Product>) {
        dataList= data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_row_main, parent, false)
        return MainViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val peso = dataList[position]

        holder.produc=peso
        holder.bindView(peso)



    }



    inner class MainViewHolder(itemView: View,var produc:Product?=null) : RecyclerView.ViewHolder(itemView) {
        fun bindView(producto: Product) {

            itemView.text_viewName.text = producto.name.toString()
            itemView.text_viewId.text = producto.price.toString()

            itemView.btedit.setOnClickListener{
                produc?.let {it->
                    val directions =MainFragmentDirections.actionMainFragmentToEditFragment(it)
                    itemView.findNavController().navigate(directions)
                }


            }
            itemView.btdelete.setOnClickListener {
                produc?.let { it ->
                    val directions = MainFragmentDirections.actionMainFragmentToDeleteFragment(it)
                    itemView.findNavController().navigate(directions)
                }
            }




        }
    }

}
