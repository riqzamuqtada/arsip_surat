package com.kospin.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kospin.myapplication.model.DetailActivity
import com.kospin.myapplication.model.InputActivity
import com.kospin.myapplication.R
import com.kospin.myapplication.databinding.AdapterSuratBinding

class SuratAdapter(private val list: ArrayList<DataAdapterSurat>, val listener: Onclik) : RecyclerView.Adapter<SuratAdapter.ViewHolder>(){
    class ViewHolder(private val find: AdapterSuratBinding) : RecyclerView.ViewHolder(find.root) {
        fun bind(data: DataAdapterSurat){

            find.tvAdpNomor.setText(data.no_surat)
            find.tvAdpHal.setText(data.hal)
            find.tvAdpDivisi.setText(data.divisi)
            find.tvAdpTanggal.setText(data.tanggal)
            find.tvAdpStatus.setText(data.jenis)

            if (data.jenis == "Keluar") {
                find.lbAdpStatus.setBackgroundResource(R.drawable.lb_adp_status_keluar)
                find.lbAdpDivisi.setText("Ke")
            } else {
                find.lbAdpStatus.setBackgroundResource(R.drawable.lb_adp_status_masuk)
                find.lbAdpDivisi.setText("Dari")
            }

            find.cardAdpDetail.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).putExtra("id", data.id)
                context.startActivity(intent)
            }
            find.btnAdpUpdate.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, InputActivity::class.java).putExtra("id", data.id)
                context.startActivity(intent)
            }

        }
        val delete = find.btnAdpDelete
    }

    interface Onclik {
        fun deleteSurat(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSuratBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)
        holder.delete.setOnClickListener {
            listener.deleteSurat(data.id)
        }
    }

    fun setData(newData: List<DataAdapterSurat>){
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

}