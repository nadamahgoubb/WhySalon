package com.example.whysalon.ui.fragment.services

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.response.TimeResponse
import com.example.whysalon.databinding.FragmentCalenderBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.ui.adapter.FilterOfferAdapter
import com.example.whysalon.ui.adapter.FilterTimeAdapter
import com.example.whysalon.ui.interfaces.FilterTimeClickListener
import com.example.whysalon.util.ext.init
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@AndroidEntryPoint
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(), FilterTimeClickListener {

    lateinit var adapter: FilterTimeAdapter
    private lateinit var parent: MainActivity
    var state = 0
    override fun onFragmentReady() {
        initAdapter()
        setupUi()
        onClick()
        loadData()
    }

    private fun onClick() {
        binding.lytNext.setOnClickListener {
            if(state==1){

                findNavController().navigate(R.id.orderSucessDialog)
            }
        }
    }

    private fun loadData() {
        adapter.list = listOf(
            TimeResponse(id =6, time = "09:00 am"),
            TimeResponse(id = 1, time = "10:00 am"),
            TimeResponse(id = 2, time = "11:00 am"),
            TimeResponse(id = 3, time = "12:00 am"),
            TimeResponse(id = 4, time = "01:00 pm"),
            TimeResponse(id = 5, time = "02:00 pm")
        ) as MutableList<TimeResponse>


        adapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        adapter = FilterTimeAdapter(this)
        binding.rvFilters.init(requireContext(), adapter, 1)

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showback(true)
        parent.setTitle(resources.getString(com.example.whysalon.R.string.booking_appointment))
        //  binding.cal.setOnDayClickListener(this)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        setupCalender()

    }

    fun setupCalender() {
        //   Setting minumum and maximum dates:
        val calendarView = binding.cal
        var min = Calendar.getInstance();
        var max = Calendar.getInstance();

        calendarView.setMinimumDate(min);
        var cc = ArrayList<Calendar>();
        var c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 15)
        cc.add(c)
        c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 20)
        cc.add(c)
        c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 25)
        cc.add(c)

        c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 27)
        cc.add(c)

        c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 28)
        cc.add(c)

        c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 29)
        cc.add(c)

        c = Calendar.getInstance()
        c.set(2023, Calendar.JULY, 7)
        cc.add(c)

        calendarView.setOnDayClickListener(OnDayClickListener {
      if(it.isEnabled)            state1()


        } )
         binding.btnGotoDate.setOnClickListener {
            c = Calendar.getInstance()
            c.set(2023, Calendar.JULY, 26)
            binding.cal.setDate(c)
            state1()
        }
        //   calendarView.setMaximumDate(max);
        //   Setting disabled dates:


        calendarView.setDisabledDays(cc);
        //    Setting highlighted days:
        //    calendarView.setHighlightedDays(dd)
        //   Setting selected dates:
        var e = Calendar.getInstance()

        calendarView.setDate(e)
        //   ...or if you want to remove selected dates:

        //    calendarView.clearSelectedDays();

    }

    fun state1() {

        binding.lyt1.isVisible = false
        binding.lyt2.isVisible = true
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
    }*/
/*
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


    override fun onTimeChoosenListener(b: Boolean) {
        if (b) {
            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_black_white_border)
            binding.lytTime.isVisible = true
            state = 1
        } else {
            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_gray)
            binding.lytTime.isVisible = false
            state = 0

        }

    }
}
