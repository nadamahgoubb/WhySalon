package com.dot_jo.whysalon.ui.fragment.createOrder

import android.annotation.SuppressLint
import android.os.Build
import android.text.format.DateUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
 import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.TimesOfBarbarResponse
import com.dot_jo.whysalon.databinding.FragmentCalenderBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.FilterTimeAdapter
import com.dot_jo.whysalon.ui.interfaces.FilterTimeClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.convertPttern
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.formatDate
import com.dot_jo.whysalon.util.getDayFromDate
import com.dot_jo.whysalon.util.getMonthFromDate
import com.dot_jo.whysalon.util.getMonthNameFromDate
import com.dot_jo.whysalon.util.getYearFromDate
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calender.btn_goto_date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.properties.Delegates


@Suppress("DEPRECATION")
@AndroidEntryPoint
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(), FilterTimeClickListener {

    private var time: String? = ""
    private var date_: String? = null
    private var dateReal: String? = ""
    private var orderId by Delegates.notNull<String>()
    lateinit var adapter: FilterTimeAdapter
    private lateinit var parent: MainActivity
    private val mViewModel: CreateOrderViewModel by activityViewModels()
    var state = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFragmentReady() {
        initAdapter()
        setupUi()
        onClick()
        orderId =arguments?.getString(Constants.ORDER_ID,"").toString()
         if (orderId =="null"){
            orderId = ""
        }
        date_ =  LocalDate.now().formatDate("yyyy-MM-dd")
        mViewModel.apply {

        
                date_?.let {
          if(orderId.isNullOrEmpty())        mViewModel.getTimesReBooking(   it  )
                    else     mViewModel.getTimesReBooking(   it ,orderId)

                } 
             observe(viewState) {
                handleViewState(it)
            }
        }

    }

    private fun handleViewState(action: CreateOrderAction) {
        when (action) {
            is CreateOrderAction.ShowLoading -> {

                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is CreateOrderAction.ShowTimes -> {
                showProgress(false)
                showTimeList(action.data)
                dateReal = action.data.date
            }


            is CreateOrderAction.ShowBookingAdded -> {
                showProgress(false)
                parent.setBadge(0)
                findNavController().navigate(R.id.orderSucessDialog)


            }


            is CreateOrderAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)

                }
                showProgress(false)

            }

            else -> {

            }
        }
    }


    private fun onClick() {
        binding.lytNext.setOnClickListener {
            if (state == 1) {
                time?.let { it1 ->
                    mViewModel.barbar?.id?.let { it2 ->
                        mViewModel.date?.let { it3 ->
                            if (orderId.isNullOrEmpty()){
                                mViewModel.addBooking(
                                    it2, it3,
                                    it1,
                                )
                            }else{
                                mViewModel.addReBooking(
                                    it2, it3,
                                    it1,
                                    orderId!!
                                )
                            }

                        }
                    }
                }
            }
        }
        binding.btnGotoDate.setOnClickListener {
            var e = Calendar.getInstance()
            binding.cal.setDate(e)
            e = Calendar.getInstance()
            e.set(
                getYearFromDate(dateReal!!)!!,
                getMonthFromDate(dateReal!!)!!,
                getDayFromDate(dateReal!!)!!
            )
            date_= convertPttern(e.time)
            if (orderId.isNullOrEmpty()){
                mViewModel.getTimes(dateReal!!)
            }else{
                mViewModel.getTimesReBooking(dateReal!!,orderId!!)
            }
            binding.cal.setDate(e)

        }

    }

    private fun initAdapter() {
        adapter = FilterTimeAdapter(this)
        binding.rvTimes.init(requireContext(), adapter, 1)

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUi() {
        binding.tvName.text = arguments?.getString(Constants.BARBER).toString()
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showback(true)
        parent.setToolbarTitle(resources.getString(com.dot_jo.whysalon.R.string.booking_appointment))
        //  binding.cal.setOnDayClickListener(this)
        parent.showNotifactionFragment(false)

        parent.cardback.setOnClickListener {
            findNavController().popBackStack()
        }
        setupCalender()
        binding.tvTotal.setText(mViewModel.total + resources.getString(R.string.sr))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupCalender() {
        //   Setting minumum and maximum dates:
        val calendarView = binding.cal
        var min = Calendar.getInstance()//.add()
        var dayBefore = LocalDate.now()?.plusDays(-1)
        dayBefore?.let {
            min.set(dayBefore.year, dayBefore.monthValue - 1, dayBefore.dayOfMonth);
            calendarView.setMinimumDate(min)
        }
        calendarView.setOnDayClickListener(OnDayClickListener {
            if (it.isEnabled) {
                date_= convertPttern(it.calendar.time)
                 if (orderId.isNullOrEmpty()){
                    mViewModel.getTimes(convertPttern(it.calendar.time))
                 }else{
                    mViewModel.getTimesReBooking(convertPttern(it.calendar.time),orderId)
                    Log.d("isllam", "setupCalender2: $orderId")
                }

                binding.cal
            }

        })
    }

    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged", "SetTextI18n")
    fun showTimeList(data: TimesOfBarbarResponse) {
        if (data.date == date_) {

            if (data.times.isNullOrEmpty()) {
                binding.lytNoAvailableTimes.isVisible = true
                binding.lytTimes.isVisible = false
                binding.rvTimes.isVisible = false
            } else {
                binding.lytNoAvailableTimes.isVisible = false
                binding.lytTimes.isVisible = true
                binding.rvTimes.isVisible = true
                /* var arr : ArrayList<TimesItem> = arrayListOf()
                                       data.times?.forEach(
                                           arr.add(false, )
                                       )
                                  */

                adapter.list = data.times!!
                adapter.notifyDataSetChanged()
            }
        } else {
            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_gray)
            binding.lytTime.isVisible = false
            binding.rvTimes.isVisible = false
            state = 0

           binding.lytNoAvailableTimes.isVisible = true
            btn_goto_date.text =   resources.getString(R.string.please_go_to)+ data.date
            binding.tv3.text =resources.getString(R.string.but_you_can_book_for)+  data.date
        }

    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint(
        "SetTextI18n", "UseCompatLoadingForDrawables", "SimpleDateFormat",
        "DefaultLocale"
    )
    override fun onTimeChoosenListener(b: String?) {
        if (b == null) {
            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_gray)
            binding.lytTime.isVisible = false
             state = 0

        } else {
            time = b


            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_black_white_border)
            binding.lytTime.isVisible = true
            binding.tvTimeSelected.text =
                resources.getString(R.string.time_) + " " + LocalTime.parse(time!!).format(
                    DateTimeFormatter.ofPattern("h:mma")
                )
            binding.tvDateSelected.text =
                resources.getString(R.string.date_) + " " + getDayFromDate(mViewModel.date!!)+" "+getMonthNameFromDate(mViewModel.date!!)!!.substring(0,3)
            state = 1


        }
    }
}
