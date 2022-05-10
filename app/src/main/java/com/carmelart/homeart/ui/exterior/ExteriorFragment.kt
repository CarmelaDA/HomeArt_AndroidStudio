package com.carmelart.homeart.ui.exterior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentExteriorBinding

class ExteriorFragment : Fragment() {

    private lateinit var exteriorViewModel: ExteriorViewModel
    private var _binding: FragmentExteriorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exteriorViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ExteriorViewModel::class.java)

        _binding = FragmentExteriorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textExterior
        exteriorViewModel.text.observe(viewLifecycleOwner, Observer {
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