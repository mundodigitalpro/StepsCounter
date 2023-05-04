package com.josejordan.stepscounter

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val viewModel: StepViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var steps = 0
    private var distance: Double = 0.0
    private lateinit var stepsTextView: TextView
    private lateinit var distanceTextView: TextView
    private val activityRecognitionRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton: Button = findViewById(R.id.start_button)
        val stopButton: Button = findViewById(R.id.stop_button)

        stepsTextView = findViewById(R.id.steps_textview)
        distanceTextView = findViewById(R.id.distance_textview)

        viewModel.stepData.observe(this, Observer { stepData ->
            stepData?.let {
                stepsTextView.text = getString(R.string.steps_text, it.steps)
                distanceTextView.text = getString(R.string.distance_text, it.distance)
            }
        })

        checkAndRequestPermissions()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        startButton.setOnClickListener {
            stepSensor?.let { sensor ->
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

        stopButton.setOnClickListener {
            sensorManager.unregisterListener(this)
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val stepData = StepEntity(date = today, steps = steps, distance = distance)

            viewModel.stepData.value?.let {
                viewModel.update(stepData)
            } ?: run {
                viewModel.insert(stepData)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    activityRecognitionRequestCode
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            activityRecognitionRequestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Permiso de reconocimiento de actividad otorgado",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Permiso de reconocimiento de actividad denegado",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                steps = it.values[0].toInt()
                stepsTextView.text = getString(R.string.steps_text, steps)
                distance = calculateDistance(steps)
                distanceTextView.text = getString(R.string.distance_text, distance)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    private fun calculateDistance(steps: Int): Double {
        val stepsPerKilometer = 1250.0
        return steps / stepsPerKilometer
    }
}


