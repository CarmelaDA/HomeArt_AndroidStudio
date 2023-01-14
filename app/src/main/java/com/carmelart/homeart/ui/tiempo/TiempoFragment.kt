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
        binding.switchVentiladorSalon.isChecked = this.dataTiempo.tVentSalon == 1
        binding.switchVentiladorDormitorio.isChecked = this.dataTiempo.tVentDorm == 1
        binding.switchVentiladorOficina.isChecked = this.dataTiempo.tVentOfi == 1
        binding.switchCalefaccion.isChecked = this.dataTiempo.tCalef == 1
        binding.switchModo.isChecked = this.dataTiempo.tAuto == 1

        // MODO MANUAL/AUTOMÁTICO
        binding.switchModo.setOnCheckedChangeListener { _, isChecked ->

            if (binding.switchModo.isChecked == true){
                Toast.makeText(
                    getActivity(), "Automatización única del ventilador del salón y la calefacción central.",
                    Toast.LENGTH_LONG
                ).show()
                Toast.makeText(
                    getActivity(), "Consulte la configuración del termostato para una mejor experiencia.",
                    Toast.LENGTH_LONG
                ).show()
            }
            else{

            }

            // Bloqueo de Switches
            //binding.switchVentiladorSalon.isEnabled = !isChecked
            //binding.switchVentiladorDormitorio.isEnabled = !isChecked
            //binding.switchVentiladorOficina.isEnabled = !isChecked
            //binding.switchCalefaccion.isEnabled = !isChecked
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
        binding.switchVentiladorSalon.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorSalon.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tVentSalon = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }

        binding.switchVentiladorDormitorio.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorDormitorio.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tVentDorm = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }

        binding.switchVentiladorOficina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleVentiladorOficina.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tVentOfi = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }

        binding.switchCalefaccion.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleCalefactor.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataTiempo.tCalef = ledValue
            dbController.DataDAO().updateData(this.dataTiempo)
            this.generateDataStringAndSend(this.dataTiempo)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vTiempo: MutableList<String> = mutableListOf()

        // Todos los datos de Tiempo en el orden deseado
        vTiempo.add(data.tVentSalon.toString())
        vTiempo.add(data.tVentDorm.toString())
        vTiempo.add(data.tVentOfi.toString())
        vTiempo.add(data.tCalef.toString())

        sendDataToServer("t;$vTiempo;$token\n") // t[..., ..., ...] + token
    }
}