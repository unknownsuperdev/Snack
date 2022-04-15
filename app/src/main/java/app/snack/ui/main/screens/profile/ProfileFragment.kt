package app.snack.ui.main.screens.profile

import android.graphics.RectF
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentProfileBinding
import app.snack.model.response.MoneyEarnedByDays
import app.snack.ui.main.SharedViewModel
import app.snack.ui.main.screens.dashboard.DashboardViewModel
import app.snack.ui.view.XYMarkerView
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.showAlertDisableMobileData
import app.snack.utils.extensions.showEmailConfirmation
import app.snack.utils.extensions.toUsd
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class ProfileFragment : BindingFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel by activityViewModels<ProfileViewModel>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfileBinding::inflate

    private val onValueSelectedRectF = RectF()

    override fun setupUI() {
        binding.btnHistory.onClick { addScreen(Screen.PROFILE_PAYOUT_HISTORY) }
        setupChart()
    }

    override fun observeViewModels() {
        super.observeViewModels()
        sharedViewModel.profile.observe(viewLifecycleOwner) { it ->

            it?.let { profile ->
                binding.tvEarned.text = String.format("$%.2f", profile.currentBalance.toUsd(2))
//                binding.tvEarnings.text = String.format("$%.3f", profile.moneyEarnedPerWeek.cent.toUsd(2))
                binding.tvEarnings.text = String.format("$%.2f", profile.moneyEarnedPerWeek.toUsd(2))

                binding.progressLimit.progress = profile.completeForPayoutPercent.toInt()

                if(profile.completeForPayoutPercent >= 100f) {
                    binding.tvPayoutComplete.text = getString(R.string.payout_100_complete)
                } else {
//                    binding.tvPayoutComplete.text = String.format("Payout: %.1f%% complete", profile.completeForPayoutPercent)
                    binding.tvPayoutComplete.text = getString(R.string.payout_completed_on, profile.completeForPayoutPercent)
                }

                binding.btnProgress.onClick {
                    if(profile.completeForPayoutPercent >= 100f) {
                        if(profile.emailConfirmed) {
                            addScreen(Screen.PROFILE_PAYOUT_REQUEST)
                        } else {
                            requireActivity().showEmailConfirmation({
                                viewModel.sendConfirmation()
                            })
                        }
                    }
                }

                // last week earnings
                val lastWeekEarnings = profile.moneyEarnedPerLastWeekByDay.map { it.value }.sum()
//                binding.lastWeekEarnings.text = String.format("$%.3f", lastWeekEarnings.toUsd(2))
                binding.lastWeekEarnings.text = String.format("$%.2f", lastWeekEarnings.toUsd(2))

                val earningsCurrentWeek = profile.moneyEarnedByWeekByDay.map { it.value }.sum()
                //val earningsExists = profile.moneyEarnedByWeekByDay.firstOrNull { x -> x.sum != 0f }

                // todo mock for testing
//                val mockedData = ArrayList<MoneyEarnedByDays>()
//                mockedData.add(MoneyEarnedByDays("Monday", 0.0001f))
//                mockedData.add(MoneyEarnedByDays("Tuesday", 0.001f))
//                mockedData.add(MoneyEarnedByDays("Wednesday", 0.000f))
//                mockedData.add(MoneyEarnedByDays("Thursday", 0.000f))
//                mockedData.add(MoneyEarnedByDays("Friday", 0.000f))
//                mockedData.add(MoneyEarnedByDays("Saturday", 0.000f))
//                mockedData.add(MoneyEarnedByDays("Sunday", 0.000f))

                // todo sum testing remove
//                val mockedSum = mockedData.map { it.sum }.sum()
//                Log.d("AAA" , ">>>>>>>>>>>>>>> MOCKED SUM $mockedSum")
//                if(mockedSum > 0f) {
//                    binding.chartLayout.visibility = View.VISIBLE
//                    binding.noChartDataLayout.visibility = View.GONE
//                    setupChartData(mockedData)
//                }

                // todo return back
                if(earningsCurrentWeek > 0f || lastWeekEarnings > 0f) {
                    binding.chartLayout.visibility = View.VISIBLE
                    binding.noChartDataLayout.visibility = View.GONE
                    setupChartData(it.moneyEarnedByWeekByDay)
                } else {
                    binding.chartLayout.visibility = View.GONE
                    binding.noChartDataLayout.visibility = View.VISIBLE
                }
            }
        }
    }



    private fun setupChartData(values: List<MoneyEarnedByDays>) {
        val chart = binding.chart

        val weekDays = mapOf<String, Int>(
            "Monday"    to 0,
            "Tuesday"   to 1,
            "Wednesday" to 2,
            "Thursday"  to 3,
            "Friday"    to 4,
            "Saturday"  to 5,
            "Sunday"  to 6,
        )

        val sortedValues = values.sortedBy {
            weekDays[it.day]
        }

        val maxSumValue = sortedValues.maxOf { x -> x.value }

//        binding.tvAxis4.text = String.format("$%.4f", maxSumValue.toUsd(2))
        binding.tvAxis4.text = String.format("$%.2f", maxSumValue.toUsd(2))

        // setup chart axis

        chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = maxSumValue
            spaceTop = 0f
            spaceBottom = 0f
            setDrawZeroLine(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(true)
        }

        chart.axisRight.apply {
            axisMinimum = 0f
            axisMaximum = maxSumValue
            spaceTop = 0f
            spaceBottom = 0f
            setDrawZeroLine(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setDrawLabels(false)
        }

        val set = BarDataSet(
            sortedValues.mapIndexed { index, item -> BarEntry(index.toFloat(), item.value)  },
            ""
        )


        val barData = BarData(set).apply {
            barWidth = 0.5f
            setDrawValues(false)
        }

        val mv = XYMarkerView(requireContext())
        mv.chartView = chart

        chart.apply {
            //marker = mv
            animateY(500)
            setPinchZoom(false)
            description.isEnabled = false
            legend.isEnabled = false
            isDoubleTapToZoomEnabled = false
            setDrawGridBackground(false)
            setDrawBorders(false)
            setDrawValueAboveBar(false)
            setDrawBarShadow(true)
            setDrawValueAboveBar(false)

            this.data = barData

            // todo default
            setViewPortOffsets(1f, 1f, 1f, 1f);

            post { invalidate() }
        }

    }

    private fun setupChart() {
        val chart = binding.chart

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
            setCenterAxisLabels(true)
        }

//        chart.axisLeft.apply {
//            axisMinimum = 0f
//            axisMaximum = 20.5f
//            spaceTop = 0f
//            spaceBottom = 0f
//            setDrawZeroLine(false)
//            setDrawAxisLine(false)
//            setDrawGridLines(false)
//            setDrawLabels(true)
//        }
//
//        chart.axisRight.apply {
//            axisMinimum = 0f
//            axisMaximum = 20.5f
//            spaceTop = 0f
//            spaceBottom = 0f
//            setDrawZeroLine(false)
//            setDrawAxisLine(false)
//            setDrawGridLines(false)
//            setDrawLabels(false)
//        }
    }


}
