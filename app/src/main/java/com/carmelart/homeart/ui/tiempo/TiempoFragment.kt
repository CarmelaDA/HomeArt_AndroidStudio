package com.carmelart.homeart.ui.tiempo

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.carmelart.homeart.databinding.FragmentTiempoBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class TiempoFragment : Fragment() {

    private lateinit var tiempoViewModel: TiempoViewModel
    private lateinit var dataTiempo: DataEntity
    private var _binding: FragmentTiempoBinding? = null
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
        tiempoViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(TiempoViewModel::class.java)

        _binding = FragmentTiempoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textTiempoInterior
        tiempoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // ACTUALIZAR DATA
        this.dataTiempo = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE TIEMPO
        binding.switchVentiladorInt.isChecked = this.dataTiempo.tVentInt == 1
        binding.switchCalefaccionInt.isChecked = this.dataTiempo.tCalefInt == 1
        binding.switchVentiladorExt.isChecked = this.dataTiempo.tVentExt == 1
        binding.switchCalefaccionExt.isChecked = this.dataTiempo.tCalefExt == 1

        // MODO MANUAL/AUTOMÁTICO
        binding.switchModo.setOnCheckedChangeListener { _, isChecked ->

            // Bloqueo de Switches
            binding.switchVentiladorInt.isEnabled = !isChecked
            binding.switchVentiladorExt.isEnabled = !isChecked
            binding.switchCalefaccionInt.isEnabled = !isChecked
            binding.switchCalefaccionExt.isEnabled = !isChecked
        }

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
            val activity: TiempoFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: TiempoFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: TiempoFragment = this
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
        binding.switchVentiladorInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorInt.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tVentInt = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }

        binding.switchCalefaccionInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCalefactorInt.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tCalefInt = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }

        binding.switchVentiladorExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorExt.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tVentExt = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }

        binding.switchCalefaccionExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCalefactorExt.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tCalefExt = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vTiempo: MutableList<String> = mutableListOf()

        // Todos los datos de Tiempo en el orden deseado
        vTiempo.add(data.tVentInt.toString())
        vTiempo.add(data.tCalefInt.toString())
        vTiempo.add(data.tVentExt.toString())
        vTiempo.add(data.tCalefExt.toString())
        vTiempo.add(data.rHuerto.toString())

        sendDataToServer("t;$vTiempo;$token\n") // t[..., ..., ...] + token
    }
}