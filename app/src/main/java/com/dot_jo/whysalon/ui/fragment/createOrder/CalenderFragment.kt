package com.dot_jo.whysalon.ui.fragment.createOrder

import android.os.Build
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
import com.dot_jo.whysalon.util.convertPttern
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.Month
import java.util.*


@AndroidEntryPoint
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(), FilterTimeClickListener {

    private var time: String? =""
    lateinit var adapter: FilterTimeAdapter
    private lateinit var parent: MainActivity
    private val mViewModel: CreateOrderViewModel by activityViewModels()
    var state = 0
    override fun onFragmentReady() {
        initAdapter()
        setupUi()
        onClick()
         mViewModel.apply {
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


            }


            is CreateOrderAction.ShowBookingAdded -> {
                showProgress(false)
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
                            mViewModel.addBooking(
                                it2, it3,
                                it1
                            )
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = FilterTimeAdapter(this)
        binding.rvTimes.init(requireContext(), adapter, 1)

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showback(true)
        parent.setTitle(resources.getString(com.dot_jo.whysalon.R.string.booking_appointment))
        //  binding.cal.setOnDayClickListener(this)
        parent.showNotifactionFragment(false)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        setupCalender()
binding.tvTotal.setText(mViewModel.total+ resources.getString(R.string.sr))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setupCalender() {
        //   Setting minumum and maximum dates:
        val calendarView = binding.cal
         var min = Calendar.getInstance()//.add()
        var dayBefore= LocalDate.now()?.plusDays(-1)
      dayBefore?.let {
          min.set(  dayBefore.year, dayBefore.monthValue-1, dayBefore.dayOfMonth);
          calendarView.setMinimumDate(min)
      }
        calendarView.setOnDayClickListener(OnDayClickListener {
            if (it.isEnabled) {

                mViewModel.getTimes(convertPttern(it.calendar.time))
                binding.cal
            }

        })

        var e = Calendar.getInstance()
     //  calendarView.setDate(e)
        binding.btnGotoDate.setOnClickListener {
            /*       c = Calendar.getInstance()
                   c.set(2023, Calendar.JULY, 26)
                   binding.cal.setDate(c)*/
            //  showTimeList(action.data)
        }


    }

    fun showTimeList(data: TimesOfBarbarResponse) {
        if (data.times.isNullOrEmpty()) {
            binding.lytNoAvailableTimes.isVisible = true
            binding.lytTimes.isVisible = false
        } else {
            binding.lytNoAvailableTimes.isVisible = false
            binding.lytTimes.isVisible = true
     /* var arr : ArrayList<TimesItem> = arrayListOf()
                data.times?.forEach(
                    arr.add(false, )
                )
           */

                adapter.list =         data.times!!
                    adapter.notifyDataSetChanged()
            }
        }



    /*
    fun  setupCalender(){

      //  val calendarView = binding.cal
        val calendar = Calendar.getInstance()

    // Initial date
        calendar.set(2023, Calendar.JUNE, 1)
        val initialDate = CalendarDate(calendar.time)

    // Minimum available date
        calendar.set(2023, Calendar.MAY, 15)
        val minDate = CalendarDate(calendar.time)

    // Maximum available date
        calendar.set(2023, Calendar.JULY, 30)
        val maxDate = CalendarDate(calendar.time)

    // List of preselected dates that will be initially selected
     //   val preselectedDates: List<CalendarDate> = getPreselectedDates()

    // The first day of week
        val firstDayOfWeek = java.util.Calendar.SATURDAY

    // Set up calendar with SelectionMode.SINGLE
     //   calendarView.setupCalendar(selectionMode = ru.cleverpumpkin.calendar.CalendarView.SelectionMode.SINGLE)


    // Get selected date or null
     //   val selectedDate: CalendarDate? = calendarView.selectedDate

    // Get list with single selected date or empty list
      //  val selectedDates: List<CalendarDate> = calendarView.selectedDates
      //  val calendarView: CalendarView = binding.cal
        var min = Calendar.getInstance();
        var max = Calendar.getInstance();
    //    calendarView.setupCalendar()

      //  calendarView.setMinimumDate(min);
      //  calendarView.setMaximumDate(max);
     //   Setting disabled dates:
      var calendars   =  ArrayList<Calendar>();
     //   calendarView.setDisabledDays(calendars);
     //   Setting highlighted days:
     //   List<Calendar> calendars = new ArrayList<>();
       // calendarView.setHighlightedDays(calendars);
    //    Setting selected dates:
     //   List<Calendar> calendars = new ArrayList<>();
      //  calendarView.setSelectedDates(calendars);
    }
    */
    /*  override fun onDayClick(eventDay: EventDay) {
        showToast( notes[eventDay] )
          var ca = arrayListOf<Calendar>()
           ca.add( eventDay.calendar)
          binding.cal.selectedDates= ca
  //binding.cal.text(int) = resources.getColor(R.color.blue_500)
       *//*   val note = "data?.getStringExtra(NOTE_EXTRA) ?: return"
         val eventDay = EventDay(eventDay.calendar, requireContext().getColor(R.color.red_dark))
        notes[eventDay] = note
        binding.cal.setEvents(notes.keys.toList())*//*
    }*//*
        override fun onSelect(calendar: List<Calendar>) {
             val intent = Intent( )
            intent.putExtra("CALENDAR_EXTRA", calendar.first())
            startActivityForResult(intent, RESULT_CODE)
            showToast(calendar.first().calendarType)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
             val calendar = data?.getSerializableExtra(CALENDAR_EXTRA) as Calendar
            val eventDay = EventDay(calendar, requireContext().getColor(R.color.red_dark))
            notes[eventDay] = note
            binding.cal.setEvents(notes.keys.toList())
        }*/




    override fun onTimeChoosenListener(b: String?) {
        if (b== null) {
            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_gray)
            binding.lytTime.isVisible = false
            state = 0

        } else {
            time = b

            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_black_white_border)
            binding.lytTime.isVisible = true
            binding.tvTimeSelected.setText(resources.getString(R.string.time_)+time)
            binding.tvDateSelected.setText(resources.getString(R.string.date_)+ mViewModel.date?.let {
                convertPttern(
                    INPUT_DATE_STRING=       it
                )
            })
            state = 1

        }    }
}
