package com.carmelart.homeart

import android.support.v4.app.Fragment
import android.widget.Toast
import com.carmelart.homeart.MainActivityViewModel
import com.carmelart.homeart.databinding.FragmentIluminacionBinding
import com.carmelart.homeart.ui.iluminacion.IluminacionFragment
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException

class extensions {
}

private lateinit var mainActivityViewModel: MainActivityViewModel

fun Fragment.formatStringsForTextView()
{
    val stringLED: String
    if (mainActivityViewModel.LED_ == 0) stringLED = "0"
    else stringLED = "1"

    mainActivityViewModel.mensaje = "SET_LED:&stringLED \n"
    //binding.textSet.text = "SET_LED:$stringLED \n"
}

fun Fragment.sendDataToServer(message: String)
{
    try
    {
        var address = "172.20.10.5"
        var port = 80

        // Se necesita de un HOST y un PORT, se conecta el SERVERPOCKET al puerto 7777
        val socket = Socket()
        socket.soTimeout = mainActivityViewModel.timeout
        socket.connect(InetSocketAddress(address, port), mainActivityViewModel.timeout)
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

        /*
        Toast.makeText(
            getActivity(), message,
            Toast.LENGTH_SHORT
        ).show()
        */

        println("Cerrando socket")
        socket.close()
    }

    catch (e: SocketException)
    {
        e.printStackTrace()
        //val activity: IluminacionFragment = this
        Toast.makeText(
            getActivity(), "Conexión Wi-Fi fallida",
            Toast.LENGTH_SHORT
        ).show()
    }

    catch (e: Exception)
    {
        e.printStackTrace()
        //val activity: IluminacionFragment = this
        Toast.makeText(
            getActivity(), "Servidor caído",
            Toast.LENGTH_SHORT
        ).show()
    }
}
