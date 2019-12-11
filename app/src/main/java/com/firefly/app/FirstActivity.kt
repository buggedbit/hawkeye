package com.firefly.app

import android.content.Intent
import android.os.Bundle
import android.os.AsyncTask
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_first.*
import java.io.IOException


class FirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        // tetheredVisualOdometerBtn
        tetheredVisualOdometerBtn.setOnClickListener({
            val ipAddress = screenIPAddressView.text.toString()
            val port = screenPortView.text.toString()
            ConnectToComputerTask().execute(ipAddress, port)
        })
        // untetheredVisualOdometerBtn
        untetheredVisualOdometerBtn.setOnClickListener({
            SocketHolder.bypass()
            goToVisualOdometerActivity()
        })
        // gotoInertialOdometerBtn
        gotoInertialOdometerBtn.setOnClickListener({
            goToInertialOdometerActivity()
        })
    }

    fun goToVisualOdometerActivity() {
        val activity = Intent(this@FirstActivity, VisualOdometerActivity::class.java)
        startActivity(activity)
    }

    fun goToInertialOdometerActivity() {
        val activity = Intent(this@FirstActivity, InertialOdometerActivity::class.java)
        startActivity(activity)
    }

    private inner class ConnectToComputerTask : AsyncTask<String, String, Int>() {

        override fun doInBackground(params: Array<String>): Int? {
            return try {
                Thread.sleep(1000)
                val ipAddress = params[0]
                val port = params[1].toInt()
                SocketHolder.connect(ipAddress, port)
                0
            } catch (e: IOException) {
                e.printStackTrace()
                -1
            }
        }

        override fun onPostExecute(response: Int?) {
            if (response!! == 0) {
                Toast.makeText(
                    this@FirstActivity,
                    "Connected to firefly listener",
                    Toast.LENGTH_SHORT
                ).show()
                goToVisualOdometerActivity()
            } else {
                Toast.makeText(
                    this@FirstActivity,
                    "Cannot connect to firefly listener",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}