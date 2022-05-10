package com.carmelart.homeart.ui.seguridad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Switch
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.provider.CalendarContract
import androidx.navigation.fragment.findNavController
import com.carmelart.homeart.R
import com.carmelart.homeart.databinding.FragmentSeguridadBinding
import androidx.appcompat.app.AppCompatActivity

class SeguridadFragment : Fragment() {

    private lateinit var seguridadViewModel: SeguridadViewModel
    private var _binding: FragmentSeguridadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        seguridadViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(SeguridadViewModel::class.java)

        _binding = FragmentSeguridadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSeguridad
        seguridadViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })


        // SWITCHES

        binding.switchSeguridadInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleSeguridadInt.isChecked = isChecked
        }

        binding.switchSeguridadExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleSeguridadExt.isChecked = isChecked
        }

        // FIN onCreateView
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}