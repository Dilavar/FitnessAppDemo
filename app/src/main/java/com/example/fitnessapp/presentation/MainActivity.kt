package com.example.fitnessapp.presentation

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.domain.utils.StepsState
import com.example.fitnessapp.presentation.viewmodels.DashBoardActivityViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var barChart: BarChart? = null
    val viewModel: DashBoardActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBarChart()
        viewModel.getSteps("")
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.state.collectLatest {
                if(it.isLoading){
                    Toast.makeText(this@MainActivity,"LOADING",Toast.LENGTH_LONG).show()
                }else if(it.steps?.isNotEmpty() == true){
                    Toast.makeText(this@MainActivity,"NOT EMPTY",Toast.LENGTH_LONG).show()
                }else if(it.error.isEmpty()){
                    Toast.makeText(this@MainActivity," EMPTY",Toast.LENGTH_LONG).show()
                }
            }
        }



    }

    private fun initBarChart() {
        barChart = findViewById(R.id.mChart)
        barChart?.setDrawBarShadow(false)
        barChart?.setDrawValueAboveBar(true)
        barChart?.setDescription(Description())
        barChart?.setMaxVisibleValueCount(48)
        barChart?.setPinchZoom(true)
        barChart?.setDrawGridBackground(false)
        barChart?.setBackgroundResource(android.R.drawable.screen_background_light_transparent)

        val xl = barChart?.getXAxis()
        barChart?.getXAxis()?.setAxisMinValue(0f);
        xl?.granularity = 1f
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setCenterAxisLabels(false)

        xl?.setValueFormatter(object : IndexAxisValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                var temp = value.toInt()
                if (temp >= 12) {
                    if (temp == 12) {
                        return "12:00 PM".trim()
                    } else {
                        temp -= 12
                    }
                    if (temp == 12) {
                        return "${temp}\n:00 AM".trim()
                    }
                    return "${temp}\n:00 PM".trim()
                } else {
                    if (temp == 0) {
                        return "12:00 AM".trim()
                    }
                    return "0${temp}\n:00 AM".trim()
                }

            }
        })

        val leftAxis = barChart?.getAxisLeft()

        leftAxis?.setDrawGridLines(false)
        leftAxis?.spaceTop = 30f
        leftAxis?.setAxisMinValue(0f) // this replaces setStartAtZero(true

        barChart?.getAxisRight()?.isEnabled = false

        val barWidth = 0.4f // x2 dataset
        val startYear = 0
        val endYear = 24
        val yVals1: MutableList<BarEntry> = ArrayList()

        for (i in startYear..endYear) {
            yVals1.add(BarEntry(i.toFloat(), (i * 10).toFloat()))
            yVals1.add(BarEntry(i.toFloat() + .5f, (i * 20).toFloat()))

            viewModel.getSteps()
        }

        val set1: BarDataSet
        if (barChart?.data != null && barChart?.data!!.dataSetCount > 0) {
            set1 = barChart?.data!!.getDataSetByIndex(0) as BarDataSet
            set1.values = yVals1
            barChart?.data!!.notifyDataChanged()
            barChart?.notifyDataSetChanged()
        } else {
            // create 2 datasets with different types
            set1 = BarDataSet(yVals1, "")
            set1.color = Color.rgb(104, 241, 175)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            barChart?.setData(data)
        }
        barChart?.description?.isEnabled = false
        barChart?.barData?.barWidth = barWidth
//        barChart?.groupBars(0f, groupSpace, barSpace)
        barChart?.invalidate()
        barChart?.animateXY(1000, 3000)
    }

}