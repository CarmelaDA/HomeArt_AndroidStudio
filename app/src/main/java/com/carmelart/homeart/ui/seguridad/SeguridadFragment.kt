package com.carmelart.homeart.ui.seguridad

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.carmelart.homeart.database.DataEntity
import com.carmelart.homeart.databinding.FragmentSeguridadBinding
import com.carmelart.homeart.dbController
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.util.*

class SeguridadFragment : Fragment() {

    private lateinit var seguridadViewModel: SeguridadViewModel
    private lateinit var dataSeg: DataEntity
    private var _binding: FragmentSeguridadBinding? = null
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
        this.bindingManagement()

        // ACTUALIZAR DATA
        this.dataSeg = dbController.DataDAO().getData()

        // TODAS LA VARIABLES DE SEGURIDAD
        binding.switchSeguridadInt.isChecked = this.dataSeg.segInt == 1
        binding.switchSeguridadExt.isChecked = this.dataSeg.segExt == 1
        binding.switchModoSeguridad.isChecked = this.dataSeg.segAuto == 1

        // MODO MANUAL/AUTOMÁTICO
        binding.switchModoSeguridad.setOnCheckedChangeListener { _, isChecked ->

            if (binding.switchModoSeguridad.isChecked){
                if((!this.dataSeg.bEncendidoAlarma)||(!this.dataSeg.bApagadoAlarma)){
                    Toast.makeText(
                        activity, "Se requieren acciones en la configuración.",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.switchModoSeguridad.isChecked = false
                }
                else {
                    Toast.makeText(
                        activity, "Horario alarma:\n${this.dataSeg.tEncendidoAlarma}   - ${this.dataSeg.tApagadoAlarma}",
                        Toast.LENGTH_LONG
                    ).show()
                    Toast.makeText(
                        activity, "Consulte la configuración de las alarmas para una mejor experiencia.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            // Bloqueo de Switches
            binding.switchSeguridadInt.isEnabled = !isChecked
            binding.switchSeguridadExt.isEnabled = !isChecked

            var segValue = 0
            if (isChecked)
                segValue = 1
            this.dataSeg.segAuto = segValue
            dbController.DataDAO().updateData(this.dataSeg)
            //this.generateDataStringAndSend(this.dataSeg)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            val activity: SeguridadFragment = this
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

    fun bindingManagement() {

        binding.switchSeguridadInt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleSeguridadInt.isChecked = isChecked
            var segValue = 0
            if (isChecked)
                segValue = 1
            this.dataSeg.segInt = segValue
            dbController.DataDAO().updateData(this.dataSeg)
            this.generateDataStringAndSend(this.dataSeg)
        }

        binding.switchSeguridadExt.setOnCheckedChangeListener { _, isChecked ->
            binding.toggleSeguridadExt.isChecked = isChecked
            var segValue = 0
            if (isChecked)
                segValue = 1
            this.dataSeg.segExt = segValue
            dbController.DataDAO().updateData(this.dataSeg)
            this.generateDataStringAndSend(this.dataSeg)
        }

        // Alarma Automática
        object : CountDownTimer(86400000, 60000) { // Durante 1 día, cada minuto

            override fun onTick(millisUntilFinished: Long) {

                val hOn = 10 * dataSeg.tEncendidoAlarma[2].digitToInt() + dataSeg.tEncendidoAlarma[3].digitToInt()
                val mOn = 10 * dataSeg.tEncendidoAlarma[5].digitToInt() + dataSeg.tEncendidoAlarma[6].digitToInt()
                val hOff = 10 * dataSeg.tApagadoAlarma[2].digitToInt() + dataSeg.tApagadoAlarma[3].digitToInt()
                val mOff = 10 * dataSeg.tApagadoAlarma[5].digitToInt() + dataSeg.tApagadoAlarma[6].digitToInt()

                if (dataSeg.segAuto == 1){

                    if (dataSeg.bEncendidoAlarma && dataSeg.bApagadoAlarma){

                        val c = Calendar.getInstance()

                        var auxOff = hOff
                        if (hOff < hOn) // Si el horario contiene las 00:00h
                            auxOff = hOff+24

                        var auxOn = 0
                        if (hOn == hOff) // Si la hora de apagado y encendido es la misma
                            auxOn = 1

                        if (((auxOn == 0) && (c.get(Calendar.HOUR_OF_DAY) > hOn) && (c.get(Calendar.HOUR_OF_DAY) < auxOff)) // Alarma activada
                            || ((auxOn == 0) &&(c.get(Calendar.HOUR_OF_DAY) == hOn) && (c.get(Calendar.MINUTE) >= mOn))
                            || ((auxOn == 0) && (c.get(Calendar.HOUR_OF_DAY) == hOff) && (c.get(Calendar.MINUTE) <= mOff))
                            || ((auxOn == 1) && (c.get(Calendar.HOUR_OF_DAY) == hOff) && (c.get(Calendar.MINUTE) <= mOff) && (c.get(Calendar.MINUTE) >= mOn)) ){

                            dataSeg.segExt = 1
                            dataSeg.segInt = 1
                            dbController.DataDAO().updateData(dataSeg)
                            generateDataStringAndSend(dataSeg)

                            /*Toast.makeText(
                                getActivity(),"ALARMA ACTIVADA",
                                Toast.LENGTH_LONG
                            ).show()*/
                        }

                        else { // Alarma desactivada

                            dataSeg.segExt = 0
                            dataSeg.segInt = 0
                            dbController.DataDAO().updateData(dataSeg)
                            generateDataStringAndSend(dataSeg)

                            /*Toast.makeText(
                                getActivity(),"alarma desactivada",
                                Toast.LENGTH_LONG
                            ).show()*/
                        }
                    }
                }
            }

            override fun onFinish() {
            }

        }.start()
    }

    fun generateDataStringAndSend(data: DataEntity) {
        var vSeg: MutableList<String> = mutableListOf()

        // Todos los datos de Seguridad en el orden deseado
        vSeg.add(data.segInt.toString())
        vSeg.add(data.segExt.toString())
        //vSeg.add(data.segAuto.toString())

        sendDataToServer("s;$vSeg;$token\n") // s[..., ..., ...] + token
    }
}