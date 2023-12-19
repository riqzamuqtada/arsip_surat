package com.kospin.arsipsurat.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kospin.arsipsurat.model.DetailActivity
import com.kospin.arsipsurat.model.InputActivity
import com.kospin.arsipsurat.R
import com.kospin.arsipsurat.databinding.AdapterSuratBinding
import com.kospin.arsipsurat.utils.PublicFunction
import com.kospin.arsipsurat.viewmodel.SuratViewModel

class SuratAdapter(private val list: ArrayList<DataAdapterSurat>,private val viewModel: SuratViewModel) : RecyclerView.Adapter<SuratAdapter.ViewHolder>(){
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

            val context = itemView.context

            find.cardAdpDetail.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).putExtra("id", data.id)
                context.startActivity(intent)
            }
            find.btnAdpUpdate.setOnClickListener {
                val intent = Intent(context, InputActivity::class.java).putExtra("id", data.id)
                context.startActivity(intent)
            }

        }
        val delete = find.btnAdpDelete
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
        val context = holder.itemView.context
        holder.bind(data)
        holder.delete.setOnClickListener {

            val dataSrt = viewModel.getById(data.id)
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Hapus Arsip Surat")
            builder.setMessage("Apakah anda yakin ingin menghapus\n\"${data.hal}\" ?")

            builder.setPositiveButton("Hapus") { dialog, _ ->
                viewModel.deleteSrt(dataSrt)
                dialog.dismiss()
                notifyItemRemoved(holder.adapterPosition)
                PublicFunction.alert("Data Arsip Surat berhasil dihapus!", context)
            }

            builder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            // Ambil tombol positif setelah dialog ditampilkan
            dialog.setOnShowListener {
                val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setTextColor(ContextCompat.getColor(context, R.color.clr_red))
            }
            dialog.show()

        }
    }

    fun setData(newData: List<DataAdapterSurat>){
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

}