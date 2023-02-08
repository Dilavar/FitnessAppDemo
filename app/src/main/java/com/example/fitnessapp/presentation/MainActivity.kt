package com.example.fitnessapp.presentation

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.fitnessapp.R
import com.example.fitnessapp.common.Constants
import com.example.fitnessapp.domain.model.Compare
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
        initBarChart()
        initStepsFlow()
        initWeeklyStepFlow()
        initMonthStepsFlow()
        viewModel.getSteps()
    }

    private fun initBarChart() {
        barChart = findViewById(R.id.mChart)
        barChart?.setDrawBarShadow(false)
        barChart?.setDrawValueAboveBar(true)
        barChart?.description = Description()
        barChart?.setMaxVisibleValueCount(48)
        barChart?.setPinchZoom(true)
        barChart?.setDrawGridBackground(false)
        barChart?.setBackgroundResource(android.R.drawable.screen_background_light_transparent)
        barChart?.xAxis?.setAxisMinValue(0f);

        barChart?.axisRight?.isEnabled = false
        barChart?.axisLeft?.isEnabled = true
        barChart?.description?.isEnabled = false
        val leftAxis = barChart?.axisRight
        leftAxis?.setDrawGridLines(true)
        leftAxis?.labelCount = 3
        leftAxis?.spaceTop = 50f
        leftAxis?.spaceMax = 50f
        leftAxis?.spaceMin = 50f
    }

    private fun initWeeklyStepFlow() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.weeklyDatastate.collectLatest {
                if (it.isLoading) {
                    Toast.makeText(this@MainActivity, "LOADING STEPS", Toast.LENGTH_LONG).show()
                } else if (it.steps?.isNotEmpty() == true) {
                    initWeekBarChart(it.steps)
                } else if (it.error.isNotEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Some thing went wrong! ",
                        Toast.LENGTH_LONG
                    ).show()
//                        viewModel.insertDummySteps()

                }
            }
        }
    }

    private fun initMonthStepsFlow() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.monthWeekDatastate.collectLatest {
                if (it.isLoading) {
                    Toast.makeText(this@MainActivity, "LOADING STEPS", Toast.LENGTH_LONG).show()
                } else if (it.steps?.isNotEmpty() == true) {
                    initMonthBarChart(it.steps)
                } else if (it.error.isNotEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Some thing went wrong! ",
                        Toast.LENGTH_LONG
                    ).show()
//                        viewModel.insertDummySteps()

                }
            }
        }
    }

    private fun initStepsFlow() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.state.collectLatest {
                if (it.isLoading) {
                    Toast.makeText(this@MainActivity, "LOADING STEPS", Toast.LENGTH_LONG).show()
                } else if (it.steps?.isNotEmpty() == true) {
                    initDayBarChart(it.steps.reversed())
                } else if (it.error.isNotEmpty()) {
                    if (it.error == "404") {
                        Toast.makeText(
                            this@MainActivity,
                            "Inserting dummy records! ",
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.insertDummySteps()
                    }
                }
            }
        }
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
        tabLayout.addTab(
            tabLayout.newTab().setId(Constants.MONTH)
                .setText(resources.getText(R.string.tab_title_mont))
        );
        tabLayout.tabGravity = TabLayout.GRAVITY_CENTER;
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.id) {
                    Constants.DAY -> viewModel.getSteps()
                    Constants.WEEK -> viewModel.getWeekSteps()
                    Constants.MONTH -> viewModel.getMonthSteps()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }


    private fun initWeekBarChart(list: List<Steps>) {
        barChart?.xAxis?.setLabelCount(7, /*force: */true)
        val xl = barChart?.xAxis
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setDrawGridLines(false)
        xl?.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return XAxisUtils.getWeekXAxisUtils(value.toInt())
            }
        }
        xl?.setCenterAxisLabels(true)
        ;
        val yVals1: MutableList<BarEntry> = ArrayList()
        var j = 0
        for (i in list.sortedBy { it.timeStamp }) {
            yVals1.add(BarEntry(j.toFloat(), i.steps.toFloat()))
            j += 1
        }
        val set1: BarDataSet

        set1 = BarDataSet(yVals1, "")
        set1.setDrawValues(false)

        set1.color = Color.rgb(104, 241, 175)
        val dataSets = ArrayList<IBarDataSet>()

        dataSets.add(set1)
        val data = BarData(dataSets)
        barChart?.data = data
        barChart?.setFitBars(true)
        barChart?.barData?.barWidth = 0.4f
        barChart?.invalidate()
        barChart?.setVisibleXRangeMaximum(7f);
        barChart?.animateXY(1000, 3000)
    }

    private fun initDayBarChart(list: List<Steps>) {
        barChart?.xAxis?.setLabelCount(7, /*force: */true)
        val xl = barChart?.xAxis
        xl?.granularity = 1f
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setCenterAxisLabels(false)
        xl?.setDrawGridLines(false)
        xl?.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return XAxisUtils.getXAxisUtils(value.toInt())
            }
        }
        val yVals1: MutableList<BarEntry> = ArrayList()
        var j = 0
        for (i in list.sortedBy { it.timeStamp }) {
            j += 1
            yVals1.add(BarEntry(j.toFloat(), i.steps.toFloat()))
        }
        val set1: BarDataSet

        set1 = BarDataSet(yVals1, "")
        set1.setDrawValues(false)
        set1.color = Color.rgb(104, 241, 175)
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(set1)
        val data = BarData(dataSets)
        barChart?.data = data


        barChart?.barData?.barWidth = 0.3f
        barChart?.invalidate()
        barChart?.animateXY(1000, 3000)
    }

    private fun initMonthBarChart(list: List<Steps>) {
        initBarChart()
        barChart?.xAxis?.setLabelCount(4, /*force: */true)
        val xl = barChart?.xAxis
        xl?.granularity = 1f
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setCenterAxisLabels(false)
        xl?.setDrawGridLines(false)
        xl?.valueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return XAxisUtils.getMonthWeeksUtils(value.toInt())
            }
        }

        var max = list?.reduce(Compare::max)
        var min = list?.reduce(Compare::min)

        val dataSets = ArrayList<IBarDataSet>()

        for (step in list) {
            val week: MutableList<BarEntry> = ArrayList()
            week.add(BarEntry(step.week.toFloat(), step.steps.toFloat()))

            if (step.steps == max.steps) {
                var set = BarDataSet(week, "MAX")
                set.setColors(Color.GREEN)
                set.setDrawValues(false)
                dataSets.add(set)
            } else if (step.steps == min.steps) {
                var set = BarDataSet(week, "MIN")
                set.setColors(Color.RED)
                set.setDrawValues(false)
                dataSets.add(set)
            } else {
                var set = BarDataSet(week, "")
                set.setColors(Color.CYAN)
                set.setDrawValues(false)
                dataSets.add(set)
            }

        }
        val data = BarData(dataSets)
        barChart?.data = data
        barChart?.barData?.barWidth = 0.3f
        barChart?.invalidate()
        barChart?.animateXY(1000, 3000)
    }


}