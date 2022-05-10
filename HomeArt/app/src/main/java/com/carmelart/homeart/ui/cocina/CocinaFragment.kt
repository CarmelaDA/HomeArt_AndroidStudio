package com.carmelart.homeart.ui.cocina

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import com.carmelart.homeart.databinding.FragmentCocinaBinding

class CocinaFragment : Fragment() {

    private lateinit var cocinaViewModel: CocinaViewModel
    private var _binding: FragmentCocinaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cocinaViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(CocinaViewModel::class.java)

        _binding = FragmentCocinaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCocina
        cocinaViewModel.text.observe(viewLifecycleOwner, Observer {
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