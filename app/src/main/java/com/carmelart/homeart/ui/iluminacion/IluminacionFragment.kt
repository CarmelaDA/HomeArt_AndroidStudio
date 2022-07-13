package com.carmelart.homeart.ui.iluminacion

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

import com.carmelart.homeart.databinding.FragmentIluminacionBinding
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.dbController

import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class IluminacionFragment : Fragment() {

    private lateinit var iluminacionViewModel: IluminacionViewModel
    private lateinit var dataIlum: DataEntity
    private var _binding: FragmentIluminacionBinding? = null
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
        iluminacionViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            ).get(IluminacionViewModel::class.java)

        _binding = FragmentIluminacionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // TEXTOS
        val textView: TextView = binding.textSalon
        iluminacionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        // SWITCHES
        this.bindingManagement()
        // SEEKBARS
        binding.seekBarRed.isEnabled = false
        binding.seekBarGreen.isEnabled = false
        binding.seekBarBlue.isEnabled = false
        binding.seekBarRed.max = 255
        binding.seekBarGreen.max = 255
        binding.seekBarBlue.max = 255
        // ACTUALIZAR DATA
        this.dataIlum = dbController.DataDAO().getData()
        // TODAS LAS VARIABLES DE ILUMINACIÓN
        binding.switchIluminacionSala.isChecked = this.dataIlum.luzSala == 1
        binding.switchIluminacionComedor.isChecked = this.dataIlum.luzComedor == 1
        binding.switchIluminacionAmbiente.isChecked = this.dataIlum.luzAmbiente == 1
        binding.switchIluminacionRecibidor.isChecked = this.dataIlum.luzRecibidor == 1
        binding.switchIluminacionCocina.isChecked = this.dataIlum.luzCocina == 1
        binding.switchIluminacionFregadero.isChecked = this.dataIlum.luzFregadero == 1
        binding.switchIluminacionBano.isChecked = this.dataIlum.luzBano == 1
        binding.switchIluminacionEspejo.isChecked = this.dataIlum.luzEspejo == 1
        binding.switchIluminacionDormitorio.isChecked = this.dataIlum.luzDormitorio == 1
        binding.switchIluminacionMesitaizq.isChecked = this.dataIlum.luzMesitaIzq == 1
        binding.switchIluminacionMesitadch.isChecked = this.dataIlum.luzMesitaDch == 1
        binding.switchIluminacionOficina.isChecked = this.dataIlum.luzOficina == 1
        binding.switchIluminacionGaming.isChecked = this.dataIlum.luzGaming == 1
        binding.seekBarRed.progress = this.dataIlum.luzR
        binding.seekBarGreen.progress = this.dataIlum.luzG
        binding.seekBarBlue.progress = this.dataIlum.luzB
        binding.switchIluminacionGaraje.isChecked = this.dataIlum.luzGaraje == 1
        binding.switchIluminacionJardin.isChecked = this.dataIlum.luzJardin == 1
        binding.switchIluminacionPorche.isChecked = this.dataIlum.luzPorche == 1
        binding.switchIluminacionTendedero.isChecked = this.dataIlum.luzTendedero == 1

        // MODO MANUAL/AUTOMÁTICO
        binding.switchModoIlum.setOnCheckedChangeListener { _, isChecked ->
            // Añadir lectura de LDR para encendido y apagado automático

            // Bloqueo de Switches
            binding.switchIluminacionJardin.isEnabled = !isChecked
            binding.switchIluminacionPorche.isEnabled = !isChecked
            binding.switchIluminacionTendedero.isEnabled = !isChecked

            Toast.makeText(
                getActivity(), "Iluminación en función de la luz exterior.",
                Toast.LENGTH_SHORT
            ).show()
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
            val activity: IluminacionFragment = this
            socket.close()

        } catch (e: SocketException) {
            e.printStackTrace()
            val activity: IluminacionFragment = this
            Toast.makeText(
                getActivity(), "Conexión Wi-Fi fallida",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            val activity: IluminacionFragment = this
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

        binding.switchIluminacionSala.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionSala.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzSala = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionComedor.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionComedor.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzComedor = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionAmbiente.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionAmbiente.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzAmbiente = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionRecibidor.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionRecibidor.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzRecibidor = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionCocina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionCocina.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzCocina = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionFregadero.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionFregadero.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzFregadero = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionBano.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionBano.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzBano = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionEspejo.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionEspejo.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzEspejo = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionDormitorio.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionDormitorio.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzDormitorio = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionMesitaizq.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionMesizq.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzMesitaIzq = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionMesitadch.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionMesdch.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzMesitaDch = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionOficina.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionOficina.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzOficina = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionGaming.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGaming.isChecked = isChecked

            // Bloqueo de SeekBar
            binding.seekBarRed.isEnabled = isChecked
            binding.seekBarGreen.isEnabled = isChecked
            binding.seekBarBlue.isEnabled = isChecked

            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzGaming = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.seekBarRed.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                dataIlum.luzR = seekBar.progress
                dbController.DataDAO().updateData(dataIlum)
                generateDataStringAndSend(dataIlum)
            }
        })

        binding.seekBarGreen.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                dataIlum.luzG = seekBar.progress
                dbController.DataDAO().updateData(dataIlum)
                generateDataStringAndSend(dataIlum)
            }
        })

        binding.seekBarBlue.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {

                dataIlum.luzB = seekBar.progress
                dbController.DataDAO().updateData(dataIlum)
                generateDataStringAndSend(dataIlum)
            }
        })

        binding.switchIluminacionGaraje.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionGaraje.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzGaraje = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionJardin.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionJardin.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzJardin = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionPorche.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionPorche.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzPorche = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }

        binding.switchIluminacionTendedero.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleIluminacionTendedero.isChecked = isChecked
            var ledValue = 0
            if (isChecked)
                ledValue = 1
            this.dataIlum.luzTendedero = ledValue
            dbController.DataDAO().updateData(this.dataIlum)
            this.generateDataStringAndSend(this.dataIlum)
        }
    }

    private fun generateDataStringAndSend(data: DataEntity) {
        var vIlum: MutableList<String> = mutableListOf()

        // Todos los datos de Iluminación en el orden deseado
        vIlum.add(data.luzSala.toString())
        vIlum.add(data.luzComedor.toString())
        vIlum.add(data.luzAmbiente.toString())
        vIlum.add(data.luzRecibidor.toString())
        vIlum.add(data.luzCocina.toString())
        vIlum.add(data.luzFregadero.toString())
        vIlum.add(data.luzBano.toString())
        vIlum.add(data.luzEspejo.toString())
        vIlum.add(data.luzDormitorio.toString())
        vIlum.add(data.luzMesitaIzq.toString())
        vIlum.add(data.luzMesitaDch.toString())
        vIlum.add(data.luzOficina.toString())
        vIlum.add(data.luzGaming.toString())

        if (dbController.DataDAO().getData().luzR < 10) {
            vIlum.add("00${data.luzR.toString()}")
        }
        else if (dbController.DataDAO().getData().luzR < 100) {
            vIlum.add("0${data.luzR.toString()}")
        }
        else {
            vIlum.add(data.luzR.toString())
        }
        if (dbController.DataDAO().getData().luzG < 10) {
            vIlum.add("00${data.luzG.toString()}")
        }
        else if (dbController.DataDAO().getData().luzG < 100) {
            vIlum.add("0${data.luzG.toString()}")
        }
        else {
            vIlum.add(data.luzG.toString())
        }
        if (dbController.DataDAO().getData().luzB < 10) {
            vIlum.add("00${data.luzB.toString()}")
        }
        else if (dbController.DataDAO().getData().luzB < 100) {
            vIlum.add("0${data.luzB.toString()}")
        }
        else {
            vIlum.add(data.luzB.toString())
        }

        vIlum.add(data.luzGaraje.toString())
        vIlum.add(data.luzJardin.toString())
        vIlum.add(data.luzPorche.toString())
        vIlum.add(data.luzTendedero.toString())

        sendDataToServer("i;$vIlum;$token\n") // i[..., ..., ...] + token
    }
}