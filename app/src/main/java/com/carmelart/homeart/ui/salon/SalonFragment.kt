package com.carmelart.homeart.ui.salon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentSalonBinding

class SalonFragment : Fragment() {

    private lateinit var salonViewModel: SalonViewModel
    private var _binding: FragmentSalonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        salonViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(SalonViewModel::class.java)

        _binding = FragmentSalonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSalon
        salonViewModel.text.observe(viewLifecycleOwner, Observer {
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