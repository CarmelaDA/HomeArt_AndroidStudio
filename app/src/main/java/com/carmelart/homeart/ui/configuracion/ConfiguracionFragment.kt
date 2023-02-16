package com.carmelart.homeart.ui.configuracion

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.carmelart.homeart.TimePicker

import com.carmelart.homeart.databinding.FragmentConfiguracionBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException


class ConfiguracionFragment() : Fragment() {

    private lateinit var ajustesViewModel: ConfiguracionViewModel
    private lateinit var dataAjustes: DataEntity
    private var _binding: FragmentConfiguracionBinding? = null
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
        ajustesViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(ConfiguracionViewModel::class.java)

        _binding = FragmentConfiguracionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        //val textView: TextView = binding.textConfiguracion
        //ajustesViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        // SWITCHES
        //this.bindingManagement()

        // ACTUALIZAR DATA
        this.dataAjustes = dbController.DataDAO().getData()

        // TODAS LAS VARIABLES DE CONFIGURACIÓN

        // HORARIOS
        binding.editTextEncendidoAlarma.setOnClickListener { selectHoraEncendido() }
        val textIni = "  00:00 h"

        if (dataAjustes.tEncendidoAlarma == "  00000") {
            binding.editTextEncendidoAlarma.setText(textIni)
        } else {
            binding.editTextEncendidoAlarma.setText(this.dataAjustes.tEncendidoAlarma)
        }

        binding.editTextApagadoAlarma.setOnClickListener { selectHoraApagado() }
        if (dataAjustes.tApagadoAlarma == "  00000") {
            binding.editTextApagadoAlarma.setText(textIni)
        } else {
            binding.editTextApagadoAlarma.setText(this.dataAjustes.tApagadoAlarma)
        }

        // HUMEDAD
        binding.pickerRhMinimo.minValue = 0
        binding.pickerRhMinimo.maxValue = 100
        binding.pickerRhMaximo.minValue = 0
        binding.pickerRhMaximo.maxValue = 100

        if (this.dataAjustes.rhMinHuerto == 0) {
            binding.pickerRhMinimo.value = 30
        } else {
            binding.pickerRhMinimo.value = this.dataAjustes.rhMinHuerto
        }

        if (this.dataAjustes.rhMaxHuerto == 0) {
            binding.pickerRhMaximo.value = 50
        } else {
            binding.pickerRhMaximo.value = this.dataAjustes.rhMaxHuerto
        }

        binding.pickerRhMinimo.setOnValueChangedListener { _, _, newVal ->
            selectMinHuerto(newVal)
        }
        binding.pickerRhMaximo.setOnValueChangedListener { _, _, newVal ->
            selectMaxHuerto(newVal)
        }

        // TERMOSTATO
        binding.editTextVentEncendido.setText(this.dataAjustes.ventEnc)
        binding.editTextVentApagado.setText(this.dataAjustes.ventApa)
        binding.editTextCalefEncendido.setText(this.dataAjustes.calefEnc)
        binding.editTextCalefApagado.setText(this.dataAjustes.calefApa)

        if (this.dataAjustes.ventEnc == "00.0") {
            binding.editTextVentEncendido.setText(29.5.toString())
        } else {
            binding.editTextVentEncendido.setText(this.dataAjustes.ventEnc)
        }
        if (this.dataAjustes.ventApa == "00.0") {
            binding.editTextVentApagado.setText(23.5.toString())
        } else {
            binding.editTextVentApagado.setText(this.dataAjustes.ventApa)
        }
        if (this.dataAjustes.calefEnc == "00.0") {
            binding.editTextCalefEncendido.setText(18.5.toString())
        } else {
            binding.editTextCalefEncendido.setText(this.dataAjustes.calefEnc)
        }
        if (this.dataAjustes.calefApa == "00.0") {
            binding.editTextCalefApagado.setText(22.5.toString())
        } else {
            binding.editTextCalefApagado.setText(this.dataAjustes.calefApa)
        }

        binding.editTextVentEncendido.setOnClickListener{
            val ventEnc = binding.editTextVentEncendido.text.toString()
            this.dataAjustes.ventEnc = ventEnc
            dbController.DataDAO().updateData(this.dataAjustes)
            this.generateDataStringAndSend(this.dataAjustes)
        }
        binding.editTextVentApagado.setOnClickListener{
            val ventApa = binding.editTextVentApagado.text.toString()
            this.dataAjustes.ventApa = ventApa
            dbController.DataDAO().updateData(this.dataAjustes)
            this.generateDataStringAndSend(this.dataAjustes)
        }
        binding.editTextCalefEncendido.setOnClickListener{
            val calefEnc = binding.editTextCalefEncendido.text.toString()
            this.dataAjustes.calefEnc = calefEnc
            dbController.DataDAO().updateData(this.dataAjustes)
            this.generateDataStringAndSend(this.dataAjustes)
        }
        binding.editTextCalefApagado.setOnClickListener{
            val calefApa = binding.editTextCalefApagado.text.toString()
            this.dataAjustes.calefApa = calefApa
            dbController.DataDAO().updateData(this.dataAjustes)
            this.generateDataStringAndSend(this.dataAjustes)
        }

        // NOTIFICACIONES
        //binding.button.setOnClickListener()

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
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            Toast.makeText(
                activity, "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                activity, "Servidor caído",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*private fun bindingManagement() {

    }*/

    fun generateDataStringAndSend(data: DataEntity) {
        var vAjustes: MutableList<String> = mutableListOf()

        // Todos los datos de Baño en el orden deseado
        vAjustes.add(data.ventEnc.toString())
        vAjustes.add(data.ventApa.toString())
        vAjustes.add(data.calefEnc.toString())
        vAjustes.add(data.calefApa.toString())
        vAjustes.add(data.rhMinHuerto.toString())
        vAjustes.add(data.rhMaxHuerto.toString())

        sendDataToServer("a;$vAjustes;$token\n") // a[..., ..., ...] + token
    }

    // HORARIOS
    private fun selectHoraEncendido() {
        val hora = TimePicker { mostrarHoraEncendido(it) }
        hora.show(parentFragmentManager, "TimePicker")
    }

    private fun mostrarHoraEncendido(time: String) {
        binding.editTextEncendidoAlarma.setText("$time")
        this.dataAjustes.tEncendidoAlarma = time
        this.dataAjustes.bEncendidoAlarma = true
        dbController.DataDAO().updateData(this.dataAjustes)
    }

    private fun selectHoraApagado() {
        val hora = TimePicker { mostrarHoraApagado(it) }
        hora.show(parentFragmentManager, "TimePicker")
    }

    private fun mostrarHoraApagado(time: String) {
        binding.editTextApagadoAlarma.setText("$time")
        this.dataAjustes.tApagadoAlarma = time
        this.dataAjustes.bApagadoAlarma = true
        dbController.DataDAO().updateData(this.dataAjustes)
    }

    // HUERTO
    private fun selectMinHuerto(nVal: Int) {
        val min = binding.pickerRhMinimo.value
        val max = binding.pickerRhMaximo.value
        if (min >= max) {
            Toast.makeText(
                activity, "%RH MÍNIMO debe ser MENOR que %RH MÁXIMO",
                Toast.LENGTH_SHORT
            ).show()
            binding.pickerRhMinimo.value = 30
        }
        this.dataAjustes.rhMinHuerto = nVal
        this.dataAjustes.bMinHuerto = true
        dbController.DataDAO().updateData(this.dataAjustes)
        this.generateDataStringAndSend(this.dataAjustes)
    }

    private fun selectMaxHuerto(nVal: Int) {
        val min = binding.pickerRhMinimo.value
        val max = binding.pickerRhMaximo.value
        if (min >= max) {
            Toast.makeText(
                activity, "%RH MÍNIMO debe ser MENOR que %RH MÁXIMO",
                Toast.LENGTH_SHORT
            ).show()
            binding.pickerRhMaximo.value = 50
        }
        this.dataAjustes.rhMaxHuerto = nVal
        this.dataAjustes.bMaxHuerto = true
        dbController.DataDAO().updateData(this.dataAjustes)
        this.generateDataStringAndSend(this.dataAjustes)
    }
}