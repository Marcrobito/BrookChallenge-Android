package com.jamadev.brookchallenge.base

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.fitness.data.DataPoint
import com.jamadev.brookchallenge.databinding.ItemBpMeasureBinding
import com.jamadev.brookchallenge.databinding.ItemHighBpMeasureBinding

private const val TYPE_NORMAL = 0
private const val TYPE_HIGH = 1

class BloodPressureAdapter(private val data: List<DataPoint>) :
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == TYPE_HIGH) {
            return ViewHighBPHolder(
                ItemHighBpMeasureBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
        return ViewNormalBPHolder(
            ItemBpMeasureBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].getDiastolicPressure()!! > 80
            || data[position].getSystolicPressure()!! > 120
        ) TYPE_HIGH else TYPE_NORMAL
    }

    override fun getItemCount() = data.size

    class ViewNormalBPHolder(private val binding: ItemBpMeasureBinding) :
        BaseViewHolder(binding) {

        override fun bind(dataPoint: DataPoint) {
            binding.date.text = dataPoint.getDateString()
            binding.diastolicText.text = "DIA: ${dataPoint.getDiastolicPressure()}"
            binding.systolicText.text = "SYS: ${dataPoint.getSystolicPressure()}"
        }
    }

    class ViewHighBPHolder(private val binding: ItemHighBpMeasureBinding) :
        BaseViewHolder(binding) {

        override fun bind(dataPoint: DataPoint) {
            binding.date.text = dataPoint.getDateString()
            binding.diastolicText.text = "DIA: ${dataPoint.getDiastolicPressure()}"
            binding.systolicText.text = "SYS: ${dataPoint.getSystolicPressure()}"
            if (dataPoint.getDiastolicPressure()!! < 80)
                binding.diastolicText.setTextColor(Color.parseColor("#ffffff"))
            if (dataPoint.getSystolicPressure()!! < 120)
                binding.systolicText.setTextColor(Color.parseColor("#ffffff"))
        }
    }


}