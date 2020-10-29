package com.jamadev.brookchallenge.base

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.fitness.data.DataPoint

@BindingAdapter("visible")
fun View.bindVisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}


@BindingAdapter("pressure_adapter")
fun RecyclerView.setBloodPressureAdapter(data: List<DataPoint>?){
    if(data != null){
        val adapter = BloodPressureAdapter(data)
        setAdapter(adapter)
    }
}