package com.example.weatherapp
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data_class.List
import com.example.weatherapp.databinding.ModalTempCardBinding

class RecyclerAdapter(var list: ArrayList<List>,val context: Context):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    class ViewHolder(binding: ModalTempCardBinding):RecyclerView.ViewHolder(binding.root) {
        val icon = binding.modalWeatherIcon
        val temprature = binding.temp
        val desc = binding.modalWeatherTitle
        val dateTime = binding.dateTimeModal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ModalTempCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].weather.forEach {
            holder.desc.text = it.main
            it.icon.run {
                Glide.with(context).load("https://openweathermap.org/img/wn/${this}@2x.png")
                    .into(holder.icon)
            }
        }
        holder.temprature.text = list[position].main?.temp.toString()
        holder.dateTime.text = list[position].dtTxt
    }

}