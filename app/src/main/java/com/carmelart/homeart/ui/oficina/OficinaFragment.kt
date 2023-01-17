package com.carmelart.homeart.ui.oficina

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentOficinaBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.FragmentIluminacionBinding
import com.carmelart.homeart.dbController
import com.carmelart.homeart.ui.iluminacion.IluminacionFragment

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class OficinaFragment : Fragment() {

    private lateinit var oficinaViewModel: OficinaViewModel
    private lateinit var dataOfi: DataEntity
    private var _binding: FragmentOficinaBinding? = null
    private val binding get() = _binding!!
    private val timeout = 1000
    private val token = "fe5g8e2a5f4e85d2e85a7c5"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        oficinaViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(OficinaViewModel::class.java)

        _binding = FragmentOficinaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTTOS
        val textView: TextView = binding.textOficinaOficina
        oficinaViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        // SWITCHES
        this.bindingManagement()
        // SEEKBARS
        binding.seekBarRedOfi.isEnabled = false
        binding.seekBarGreenOfi.isEnabled = false
        binding.seekBarBlueOfi.isEnabled = false
        binding.seekBarRedOfi.max = 255
        binding.seekBarGreenOfi.max = 255
        binding.seekBarBlueOfi.max = 255
        // ACTUALIZAR DATA
        this.dataOfi = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE OFICINA
        binding.switchIluminacionGeneralOfi.isChecked = this.dataOfi.luzOficina == 1
        binding.switchIluminacionGamingOfi.isChecked = this.dataOfi.luzGaming == 1
        binding.seekBarRedOfi.progress = this.dataOfi.luzR
        binding.seekBarGreenOfi.progress = this.dataOfi.luzG
        binding.seekBarBlueOfi.progress = this.dataOfi.luzB
        binding.switchCortinaOficina.isChecked = this.dataOfi.vOficina == 1

        return root
    }

    private fun sendDataToServer(message: String) {
        try {
            var address = "172.20.10.12"    // Actualizar de vez en cuando
            var port = 80

            // Se necesita de un HOST y un PORT, se conecta el SERVERPOCKET al puerto 7777
            val socket = Socket()
            socket.soTimeout = timeout
            socket.connect(InetSocketAddress(address, port), timeout)
            println("CONECTADO")

            // Obtiene el flujo de salida del SOCKET
            val outputStream = socket.getOutputStream()

            // Crea un flujo de salida para sacar los datos
            val dataOutputStream = DataOutputStream(outputStream)
            println("Enviando cadena de datos por el ServerSocket")

            // Escribe el MENSAJE que se quiere enviar
            dataOutputStream.writeUTF(message)
            dataOutputStream.flush() // Envía el mensaje
            dataOutputStream.close() // Cierra el final del flujo de salida cuando se termina

            println("Cerrando socket")
            val activity: OficinaFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: OficinaFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: OficinaFragment = this
            Toast.makeText(
                getActivity(), "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindingManagement() {

        binding.switchIluminacionGeneralOfi.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGeneralOfi.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataOfi.luzOficina = ledValue
            dbController.DataDAO().updateData(this.dataOfi)
            this.generateDataStringAndSend('f', this.dataOfi)
        }

        binding.switchIluminacionGamingOfi.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGamingOfi.isChecked = isChecked

            // Bloqueo de SeekBar
            binding.seekBarRedOfi.isEnabled = isChecked
            binding.seekBarGreenOfi.isEnabled = isChecked
            binding.seekBarBlueOfi.isEnabled = isChecked

            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataOfi.luzGaming = ledValue
            dbController.DataDAO().updateData(this.dataOfi)
            this.generateDataStringAndSend('f', this.dataOfi)
        }

        binding.seekBarRedOfi.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                dataOfi.luzR = seekBar.progress
                dbController.DataDAO().updateData(dataOfi)
                generateDataStringAndSend('f', dataOfi)
            }
        })

        binding.seekBarGreenOfi.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                dataOfi.luzG = seekBar.progress
                dbController.DataDAO().updateData(dataOfi)
                generateDataStringAndSend('f', dataOfi)
            }
        })

        binding.seekBarBlueOfi.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                dataOfi.luzB = seekBar.progress
                dbController.DataDAO().updateData(dataOfi)
                generateDataStringAndSend('f', dataOfi)
            }
        })

        binding.switchCortinaOficina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCortinaOficina.isChecked = isChecked
            var ventValue = 0
            if (isChecked)
                ventValue = 1
            this.dataOfi.vOficina = ventValue
            dbController.DataDAO().updateData(this.dataOfi)
            this.generateDataStringAndSend('F', this.dataOfi)
        }
    }

    private fun generateDataStringAndSend(zona: Char, data: DataEntity) {
        var vOfi: MutableList<String> = mutableListOf()

        // Todos los datos de Oficina en el orden deseado
        vOfi.add(data.luzOficina.toString())
        vOfi.add(data.luzGaming.toString())

        if (dbController.DataDAO().getData().luzR < 10) {
            vOfi.add("00${data.luzR.toString()}")
        }
        else if (dbController.DataDAO().getData().luzR < 100) {
            vOfi.add("0${data.luzR.toString()}")
        }
        else {
            vOfi.add(data.luzR.toString())
        }
        if (dbController.DataDAO().getData().luzG < 10) {
            vOfi.add("00${data.luzG.toString()}")
        }
        else if (dbController.DataDAO().getData().luzG < 100) {
            vOfi.add("0${data.luzG.toString()}")
        }
        else {
            vOfi.add(data.luzG.toString())
        }
        if (dbController.DataDAO().getData().luzB < 10) {
            vOfi.add("00${data.luzB.toString()}")
        }
        else if (dbController.DataDAO().getData().luzB < 100) {
            vOfi.add("0${data.luzB.toString()}")
        }
        else {
            vOfi.add(data.luzB.toString())
        }

        vOfi.add(data.vOficina.toString())

        sendDataToServer("$zona;$vOfi;$token\n") // zona[..., ..., ...] + token
    }
}