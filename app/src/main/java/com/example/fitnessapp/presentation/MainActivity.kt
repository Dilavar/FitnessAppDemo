package com.example.fitnessapp.presentation

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.common.Constants
import com.example.fitnessapp.domain.model.Steps
import com.example.fitnessapp.domain.utils.XAxisUtils
import com.example.fitnessapp.presentation.viewmodels.DashBoardActivityViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.android.material.tabs.TabLayout
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
        initTabLayout()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.state.collectLatest {
                if (it.isLoading) {
                    Toast.makeText(this@MainActivity, "LOADING STEPS", Toast.LENGTH_LONG).show()
                } else if (it.steps?.isNotEmpty() == true) {
                    initBarChart(it.steps.reversed())
                } else if (it.error.isNotEmpty()) {
                    if (it.error == "404") {
                        Toast.makeText(
                            this@MainActivity,
                            "Inserting dummy records! ",
                            Toast.LENGTH_LONG
                        ).show()
//                        viewModel.insertDummySteps()
                    }
                }
            }
        }
        viewModel.getSteps()
    }

    private fun initTabLayout() {
        var tabLayout: TabLayout = findViewById(R.id.tabLayout)
        tabLayout.addTab(
            tabLayout.newTab().setId(Constants.DAY)
                .setText(resources.getText(R.string.tab_title_day))
        );
        tabLayout.addTab(
            tabLayout.newTab().setId(Constants.WEEK)
                .setText(resources.getText(R.string.tab_title_week))
        );
        tabLayout.tabGravity = TabLayout.GRAVITY_CENTER;
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.id) {
                    Constants.DAY -> viewModel.getSteps()
                    Constants.WEEK -> viewModel.getWeekSteps()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }


    private fun initBarChart(list: List<Steps>) {
        barChart = findViewById(R.id.mChart)
        barChart?.setDrawBarShadow(false)
        barChart?.setDrawValueAboveBar(true)
        barChart?.description = Description()
        barChart?.setMaxVisibleValueCount(48)
        barChart?.setPinchZoom(true)
        barChart?.setDrawGridBackground(false)
        barChart?.setBackgroundResource(android.R.drawable.screen_background_light_transparent)

        val xl = barChart?.xAxis
        barChart?.xAxis?.setAxisMinValue(0f);
        xl?.granularity = 1f
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setCenterAxisLabels(false)
        xl?.setDrawGridLines(false)
        xl?.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return XAxisUtils.getXAxisUtils(value.toInt())
            }
        }
        val leftAxis = barChart?.axisRight
        leftAxis?.setDrawGridLines(true)
        leftAxis?.labelCount = 3
        leftAxis?.spaceTop = 50f
        // this replaces setStartAtZero(true
        barChart?.xAxis?.setLabelCount(7, /*force: */true)
        barChart?.axisRight?.isEnabled = true
        barChart?.axisLeft?.isEnabled = false
        val barWidth = 0.5f // x2 dataset
        val yVals1: MutableList<BarEntry> = ArrayList()

        var j = 0

        for (i in list.sortedBy { it.timeStamp }) {
            j += 1
            yVals1.add(BarEntry(j.toFloat(), i.steps.toFloat()))
        }
        val set1: BarDataSet
        if (barChart?.data != null && barChart?.data!!.dataSetCount > 0) {
            set1 = barChart?.data!!.getDataSetByIndex(0) as BarDataSet
            set1.values = yVals1
            set1.setDrawValues(false)
            barChart?.data!!.notifyDataChanged()
            barChart?.notifyDataSetChanged()
        } else {
            // create 2 datasets with different types
            set1 = BarDataSet(yVals1, "")
            set1.setDrawValues(false)
            set1.color = Color.rgb(104, 241, 175)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            barChart?.data = data
        }
        barChart?.description?.isEnabled = false
        barChart?.barData?.barWidth = barWidth
//        barChart?.groupBars(0f, groupSpace, barSpace)
        barChart?.invalidate()
        barChart?.animateXY(1000, 3000)
    }

}