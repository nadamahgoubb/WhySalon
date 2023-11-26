package com.dot_jo.whysalon.ui.fragment.checkout


import android.annotation.SuppressLint
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.response.BarbarItem
import com.dot_jo.whysalon.data.response.TimesOfBarbarResponse
import com.dot_jo.whysalon.databinding.FragmentCheckoutBinding
import com.dot_jo.whysalon.databinding.ItemDayCalenderBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.BarbarAdapter
import com.dot_jo.whysalon.ui.adapter.FilterTimeAdapter
import com.dot_jo.whysalon.ui.fragment.createOrder.CreateOrderAction
import com.dot_jo.whysalon.ui.fragment.createOrder.CreateOrderViewModel
import com.dot_jo.whysalon.ui.interfaces.BarbarClickListener
import com.dot_jo.whysalon.ui.interfaces.FilterTimeClickListener
import com.dot_jo.whysalon.util.ArabicToEnglish
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.SpinnerHelper
import com.dot_jo.whysalon.util.SpinnerModl
import com.dot_jo.whysalon.util.convertLongToTime
import com.dot_jo.whysalon.util.convertPttern
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.formatDate
import com.dot_jo.whysalon.util.getDayFromDate
import com.dot_jo.whysalon.util.getMonthFromDate
import com.dot_jo.whysalon.util.getMonthNameFromDate
import com.dot_jo.whysalon.util.getYearFromDate
import com.dot_jo.whysalon.util.observe
import com.dot_jo.whysalon.util.toLong
import com.hbb20.CountryCodePicker
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import com.kizitonwose.calendar.view.WeekCalendarView
import java.time.LocalDate

import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekDayBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback
import kotlinx.android.synthetic.main.calendar_day_legend_container.view.lyt_children
import kotlinx.android.synthetic.main.fragment_calender.btn_goto_date
import kotlinx.android.synthetic.main.item_choose_barber.view.lyt_root
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

@AndroidEntryPoint

class CheckoutFragment : BaseFragment<FragmentCheckoutBinding>(), BarbarClickListener,
    FilterTimeClickListener, CountryCodePicker.OnCountryChangeListener {

    private var countryCode = "+966"
    private var date_: String? = null
    var enableCheckout = false

    @RequiresApi(Build.VERSION_CODES.O)
    private var today: LocalDate = LocalDate.now()
    private var orderId by Delegates.notNull<String>()
    private var dateReal: String? = ""
    var state = 0
    lateinit var adapterBarber: BarbarAdapter
    lateinit var adapterTime: FilterTimeAdapter
    private val mViewModel: CreateOrderViewModel by activityViewModels()
    private lateinit var parent: MainActivity


    override fun onFragmentReady() {
        createCal()
        setupUi()
        initAdapter()

        mViewModel.total = arguments?.getString(Constants.TOTAL)
        orderId = arguments?.getString(Constants.ORDER_ID, "").toString()
        if (orderId == "null") {
            orderId = ""
        }
        binding.tvTotal.text = mViewModel.total + resources.getString(R.string.sr)
        date_ = LocalDate.now().formatDate("yyyy-MM-dd")

        today = LocalDate.parse(date_, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        today_date = today.toString()

        mViewModel.apply {
            getBarbars()

            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getBarbars()

            binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun initAdapter() {
        adapterBarber = BarbarAdapter(this)
        binding.rvBarbars.init(requireContext(), adapterBarber, 1)

        adapterTime = FilterTimeAdapter(this)
        binding.rvTimes.init(requireContext(), adapterTime, 1)
    }

    private fun handleViewState(action: CreateOrderAction) {
        when (action) {
            is CreateOrderAction.ShowLoading -> {

                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is CreateOrderAction.ShowBarbars -> {
                showProgress(false)
                binding.lytData.isVisible = true
                action.data.barbers?.let {
                    adapterBarber.list = it
                    adapterBarber.notifyDataSetChanged()
                }
            }


            is CreateOrderAction.ShowFailureMsg -> {
                action.message?.let {
                    if (it.contains("401") == true) {
                        findNavController().navigate(R.id.loginFirstDialog)
                    } else {
                        showToast(action.message)
                    }
                }
                showProgress(false)

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

            is CreateOrderAction.ShowCuponVaildation -> {
                showProgress(false)
                showToast(resources.getString(R.string.vaild_cupon_with) + " " + action.data.coupon?.percent + " %")
                discount = action.data.coupon?.percent.toString()
                mViewModel.totalAfterDiscount =
                    ((mViewModel.total?.toDouble()
                        ?.times((100 - discount!!.toDouble())))?.div(100)).toString()
                binding.tvTotal.setText(mViewModel.totalAfterDiscount + resources.getString(R.string.sr))

            }

            else -> {

            }
        }
    }

    var discount: String? = null
    private fun showTimeList(data: TimesOfBarbarResponse) {
        if (data.date == today_date) {

            if (data.times.isNullOrEmpty()) {
                binding.lytNoAvailableTimes.isVisible = true
                binding.lytTimes.isVisible = false
                binding.rvTimes.isVisible = false
            } else {
                binding.lytNoAvailableTimes.isVisible = false
                binding.lytTimes.isVisible = true
                binding.rvTimes.isVisible = true/* var arr : ArrayList<TimesItem> = arrayListOf()
                                       data.times?.forEach(
                                           arr.add(false, )
                                       )
                                  */

                adapterTime.list = data.times!!
                adapterTime.notifyDataSetChanged()
            }
        } else {
            binding.lytShowTime.isVisible = false
            binding.rvTimes.isVisible = false
            state = 0

            binding.lytNoAvailableTimes.isVisible = true
            btn_goto_date.text = resources.getString(R.string.please_go_to) + data.date
            binding.tv23.text = resources.getString(R.string.but_you_can_book_for) + data.date
        }
    }

    var phone: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showback(true)
        parent.setToolbarTitle(resources.getString(com.dot_jo.whysalon.R.string.booking_appointment))

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.countryCodePicker.setOnCountryChangeListener(this)

        var phone = binding.etPhone.text.toString()
        if (PrefsHelper.getUserData()?.client?.phone.isNullOrEmpty()) {
            binding.lytPhone.isVisible = true
        }
        binding.etDisountCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().length > 3) {
                    mViewModel.checkCupon(charSequence.toString())    // d = charSequence.toString()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })


        binding.lytNext.setOnClickListener {
            //   if (state == 1) {
            var phone = binding.etPhone.text.toString()
            if (mViewModel.barbar == null) showToast(resources.getString(R.string.choose_the_barber))
            else if (mViewModel.date == null) showToast("" + resources.getString((R.string.choose_the_time_with_barber_s_name)) + " " + mViewModel?.barbar?.name.toString())
            else if (time == null) showToast("" + resources.getString((R.string.choose_the_time_with_barber_s_name)) + " " + mViewModel?.barbar?.name.toString())
            else if (binding.etPhone.text.toString().isNullOrEmpty()) {
                if (PrefsHelper.getUserData()?.client?.phone.isNullOrEmpty()) {
                    showToast(resources.getString(R.string.msg_empty_phone_number))
                } else {
                    phone = PrefsHelper.getUserData()?.client?.phone.toString()
                    time?.let { it1 ->
                        mViewModel.barbar?.id?.let { it2 ->
                            mViewModel.date?.let { it3 ->
                                if (orderId.isNullOrEmpty()) {
                                    mViewModel.addBooking(
                                        it2,
                                        it3,
                                        it1,
                                        Constants.cash.toString(),
                                        binding.etDisountCode.text.toString(),
                                        phone,
                                        countryCode
                                    )
                                } else {
                                    mViewModel.addReBooking(
                                        it2, it3, it1, orderId!!, Constants.cash.toString(),
                                        binding.etDisountCode.text.toString(),
                                        phone,
                                        countryCode
                                    )
                                }

                            }
                        }
                    }

                }
            } else {
                time?.let { it1 ->
                    mViewModel.barbar?.id?.let { it2 ->
                        mViewModel.date?.let { it3 ->
                            if (orderId.isNullOrEmpty()) {
                                mViewModel.addBooking(
                                    it2,
                                    it3,
                                    it1,
                                    Constants.cash.toString(),
                                    binding.etDisountCode.text.toString(),
                                    phone,
                                    countryCode
                                )
                            } else {
                                mViewModel.addReBooking(
                                    it2, it3, it1, orderId!!, Constants.cash.toString(),
                                    binding.etDisountCode.text.toString(),
                                    phone,
                                    countryCode
                                )
                            }

                        }
                    }
                }
            }


        }
        binding.btnGotoDate.setOnClickListener {
            dateReal?.let {
                today = LocalDate.of(
                    getYearFromDate(it)!!,
                    (getMonthFromDate(it)!! + 1)!!,
                    getDayFromDate(dateReal!!)!!
                )
                today_date = it
                date_ = it
            }
            selectedDates.clear()
            selectedDates.add(today)
            if (orderId.isNullOrEmpty()) {
                mViewModel.getTimes(dateReal!!)
            } else {
                mViewModel.getTimesReBooking(dateReal!!, orderId!!)
            }
            today?.let {
                weekCalendarView.scrollToDate(it)
            }
            currentMonth = today.yearMonth
            startMonth = currentMonth
            endMonth = currentMonth.plusMonths(100)

            setupWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        }

        /*    var listPayment = arrayListOf<SpinnerModl>()
    *//*    listPayment.add(SpinnerModl(Constants.visa, "فيزا", "visa"))
        listPayment.add(SpinnerModl(Constants.cash, "نقدي", "Cash"))
        listPayment.add(SpinnerModl(Constants.mada, "مدى", "mada"))
        listPayment.add(SpinnerModl(Constants.mastercard, "ماستر كارد", "mastercard"))
        listPayment.add(
            SpinnerModl(
                Constants.american_express, "اميريكان اكسربيس", "american_express"
            )
        )
        listPayment.add(SpinnerModl(Constants.complementary, "خصم استثنائي", "complementary"))*//*
        listPayment.add(SpinnerModl(Constants.cash, "الدفع نقدا", "Pay at the store"))
        listPayment.add(SpinnerModl(0, "اختر طريقة الدفع", "Select Payment method"))


        var spinneerHelper = activity?.let { SpinnerHelper(it, listPayment, true) }
        spinneerHelper?.setAdapter(binding.spinnerPayment)
        binding.spinnerPayment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    var pos = listPayment.get(position).id
                    if (pos != null) {
                        paymentId = pos
                        checkEnableCheckout()
                    }


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

//                     mViewModel.checkCupon(binding.etDisountCode.text.toString())
   */
    }

    private val weekCalendarView: WeekCalendarView get() = binding.exOneWeekCalendar

    private val selectedDates = mutableSetOf<LocalDate>()

    @RequiresApi(Build.VERSION_CODES.O)
    var daysOfWeek: List<DayOfWeek> = arrayListOf()
    var currentMonth = YearMonth.now()
    var startMonth = currentMonth
    var endMonth = currentMonth.plusMonths(100)

    @RequiresApi(Build.VERSION_CODES.O)
    fun createCal() {

        daysOfWeek = daysOfWeek()

        binding.legendLayout.children.map { it as TextView }

            //  (binding.legendLayout).lyt_children?.children?.map { it as TextView }
            ?.forEachIndexed { index, textView ->
                textView.text = daysOfWeek[index].displayText()
                textView.setTextColorRes(R.color.grey3)
            }


        currentMonth = YearMonth.now()
        startMonth = currentMonth
        endMonth = currentMonth.plusMonths(100)


        setupWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        setup()

    }

    fun getDayOfWeekFromLocale(d: DayOfWeek): WeekFields? = WeekFields.of(Locale.getDefault())


    private fun setupWeekCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class WeekDayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: WeekDay
            val textView = ItemDayCalenderBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.position == WeekDayPosition.RangeDate) {
                        dateClicked(date = day.date)
                        today_date = day.date.formatDate("yyyy-MM-dd")
                        today_date?.let {
                            mViewModel.getTimes(it)
                        }
                    }
                }
            }
        }
        weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)

            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == WeekDayPosition.RangeDate)
            }
        }
        weekCalendarView.weekScrollListener = { setup() }
        weekCalendarView.setup(
            LocalDate.now(),////////////////////////       //////////////////////////////
            endMonth.atEndOfMonth(),
            daysOfWeek.first(),
        )
        weekCalendarView.scrollToWeek(currentMonth.atStartOfMonth())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        var s = date.dayOfMonth.toString()
        textView.text = s
        if (isSelectable) {
            when {
                selectedDates.contains(date) -> {
                    textView.setTextColorRes(R.color.white)
                    textView.setBackgroundResource(R.drawable.bg_selected_black)
                }

                /*   disabledDates?.contains(date) == true -> {
                            textView.setTextColorRes(R.color.gray1)
                            textView.background = (null)
                        }
  */
                today == date -> {
                    if (selectedDates.isEmpty()) {
                        selectedDates.add(date)
                        textView.setTextColorRes(R.color.white)
                        textView.setBackgroundResource(R.drawable.bg_selected_black)
                        if (orderId.isNullOrEmpty()) {
                            mViewModel.getTimes(today.formatDate("YYYY-MM-dd"))
                        } else {
                            mViewModel.getTimesReBooking(today.formatDate("YYYY-MM-dd"), orderId!!)
                        }
                    }
                }

                else -> {
                    textView.setTextColorRes(R.color.black)
                    textView.background = null
                }
            }
        } else {
            textView.setTextColorRes(R.color.grey1)
            // textView.background = null
        }

    }

    var today_date = ""
    private fun dateClicked(date: LocalDate) {
        selectedDates.clear()
        if (selectedDates.contains(date)) {
        } else {

            selectedDates.add(date)
            today_date = date.formatDate("YYYY-MM-dd")
            today_date?.let {
                mViewModel.getTimes(it)

            }
        }
        // Refresh both calendar views..
        weekCalendarView.notifyDateChanged(date)
        weekCalendarView.notifyCalendarChanged()

        // }


    }

    @SuppressLint("SetTextI18n")
    private fun setup() {

        val week = weekCalendarView.findFirstVisibleWeek() ?: return
        // In week mode, we show the header a bit differently because
        // an index can contain dates from different months/years.
        val firstDate = week.days.first().date
        val lastDate = week.days.last().date
        if (firstDate.yearMonth == lastDate.yearMonth) {
            binding.exOneYearText.text = firstDate.year.toString()
            binding.exOneMonthText.text = firstDate.month.displayText(short = false)
        } else {
            binding.exOneMonthText.text =
                firstDate.month.displayText(short = false) + " - " + lastDate.month.displayText(
                    short = false
                )

            if (firstDate.year == lastDate.year) {
                binding.exOneYearText.text = firstDate.year.toString()
            } else {
                binding.exOneYearText.text = "${firstDate.year} - ${lastDate.year}"
            }
        }
        binding.ivNext.setOnClickListener {
            weekCalendarView.findLastVisibleDay()?.date?.let { it1 ->
                weekCalendarView.scrollToWeek(
                    it1.plusDays(1)
                )
            }
        }
        binding.ivPrev.setOnClickListener {
            weekCalendarView.findFirstVisibleDay()?.date?.let { it1 ->
                weekCalendarView.scrollToWeek(
                    it1.minusWeeks(1)
                )
            }
        }
    }

    // var  item: BarbarItem? = null
    var time: String? = null
    override fun onBarbarClickListener(item: BarbarItem?) {
// this.item= item

        mViewModel.barbar = item
        if (item == null) {
            binding.tv2.isVisible = false
            binding.lytCalender.isVisible = false
            binding.lytFillData.isVisible = false
        } else {
            item?.let {
                binding.tv2.setText(resources.getString(R.string.choose_the_time_with_barber_s_name) + " " + it.name)
                binding.tv2.isVisible = true
                binding.lytCalender.isVisible = true
                binding.lytFillData.isVisible = true
                createCal()
            }
        }
    }

    override fun onTimeChoosenListener(b: String?) {
        if (b == null) {
            binding.lytShowTime.isVisible = false
            state = 0

        } else {
            time = b


            binding.lytShowTime.isVisible = true
            binding.tvTimeSelected.text =
                resources.getString(R.string.time_) + " " + LocalTime.parse(time!!).format(
                    DateTimeFormatter.ofPattern("h:mma")
                )
            binding.tvDateSelected.text =
                resources.getString(R.string.date_) + " " + getDayFromDate(mViewModel.date!!) + " " + getMonthNameFromDate(
                    mViewModel.date!!
                )!!.substring(0, 3)
            state = 1
            checkEnableCheckout()


        }
    }/* */
fun checkEnableCheckout(){
    if (mViewModel.barbar == null) enableButoon(false)
    else if (mViewModel.date == null) enableButoon(false)
    else if (time == null) enableButoon(false)
      else {
        enableButoon(true)   }

}
    fun enableButoon(boolean: Boolean) {
        if (boolean) {

            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_black_white_border)
            binding.lytNext.isEnabled= true
        } else {
            binding.lytNext.background = resources.getDrawable(R.drawable.bg_btn_gray)
            binding.lytNext.isEnabled= false

        }
    }
    override fun onCountrySelected() {
        countryCode = "+" + binding.countryCodePicker.selectedCountryCode
    }

}