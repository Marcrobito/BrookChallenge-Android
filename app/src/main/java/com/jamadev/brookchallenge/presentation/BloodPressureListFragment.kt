package com.jamadev.brookchallenge.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamadev.brookchallenge.base.BaseFragment
import com.jamadev.brookchallenge.databinding.FragmentBloodPresureListBinding

private const val TAG = "BPressureListFragment"
class BloodPressureListFragment : BaseFragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: FragmentBloodPresureListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val manager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)

        binding = FragmentBloodPresureListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.recycler.layoutManager = manager

        return binding.root
    }



}