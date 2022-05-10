package com.carmelart.homeart.ui.huerto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import com.carmelart.homeart.databinding.FragmentHuertoBinding

class HuertoFragment : Fragment() {

    private lateinit var huertoViewModel: HuertoViewModel
    private var _binding: FragmentHuertoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        huertoViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(HuertoViewModel::class.java)

        _binding = FragmentHuertoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHuerto
        huertoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}