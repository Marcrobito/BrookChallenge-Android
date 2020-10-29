package com.jamadev.brookchallenge.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.fitness.data.DataPoint

abstract class BaseViewHolder(binding: ViewDataBinding)
    : RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(dataPoint: DataPoint)

}