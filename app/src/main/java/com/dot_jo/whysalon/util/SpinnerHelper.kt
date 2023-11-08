package com.dot_jo.whysalon.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.PrefsHelper
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

open class SpinnerModl(
    @SerializedName("id") open var id: Int? = null,
    @SerializedName("title_ar") open var titleAr: String? = null,
    @SerializedName("title_en") open var titleEn: String? = null
) : Any()

class SpinnerHelper(private val context: Context, list: List<SpinnerModl>, hasHint: Boolean) {
    private var adapter: ArrayAdapter<String?>? = null
    private var hasHint = false
    private val list: List<SpinnerModl>
    private var position = 0
    fun setAdapter(spinner: Spinner) {
        adapter = object : ArrayAdapter<String?>(context, R.layout.simple_spinner_item) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    (v.findViewById<View>(android.R.id.text1) as TextView).hint =
                        getItem(
                            count
                        )
                    (v.findViewById<View>(android.R.id.text1) as TextView).setTextColor(ViewCompat.MEASURED_STATE_MASK)
                    (v.findViewById<View>(android.R.id.text1) as TextView).setHintTextColor(
                        context.resources.getInteger(R.color.gray_400)
                    )
                    (v.findViewById<View>(android.R.id.text1) as TextView).highlightColor =
                        ViewCompat.MEASURED_STATE_MASK
                }
                (v.findViewById<View>(android.R.id.text1) as TextView).setTextColor(ViewCompat.MEASURED_STATE_MASK)
                (v.findViewById<View>(android.R.id.text1) as TextView).setHintTextColor(ViewCompat.MEASURED_STATE_MASK)
                (v.findViewById<View>(android.R.id.text1) as TextView).highlightColor =
                    ViewCompat.MEASURED_STATE_MASK
                return v
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getDropDownView(position, convertView, parent)
                (v.findViewById<View>(android.R.id.text1) as TextView).setTextColor(ViewCompat.MEASURED_STATE_MASK)
                (v.findViewById<View>(android.R.id.text1) as TextView).setHintTextColor(ViewCompat.MEASURED_STATE_MASK)
                (v.findViewById<View>(android.R.id.text1) as TextView).highlightColor =
                    ViewCompat.MEASURED_STATE_MASK
                return v
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        (adapter as ArrayAdapter<String?>).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        for (i in list.indices) {
            var s: SpinnerModl = list[i]
            if (PrefsHelper.getLanguage() == Constants.EN) (adapter as ArrayAdapter<String?>).add(s.titleEn)
            else (adapter as ArrayAdapter<String?>).add(s.titleAr)
        }
        spinner.adapter = adapter
        adapter?.getCount()?.let { spinner.setSelection(it) }
        spinner.onItemSelectedListener = onItemSelected()
    }

    inner class onItemSelected : AdapterView.OnItemSelectedListener {
        constructor() {

        }

        constructor(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {

            var id = JSONObject(list[i].toString()).getInt("id")

            position = id
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        }

        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    }


     fun getIndexOf( value: Int): Int {
        var index = 0
        val count: Int? = adapter?.getCount()
        if(count!= null ){
        while (index < count) {
            var s: SpinnerModl = list[index]
            if (s.id ?. equals(value) == true) {
                return index
            }
            ++index
        }
        }
        return -1
    }

    fun setPosition(position: Int) {}
    fun notifyDataChange() {
        adapter!!.notifyDataSetChanged()
    }

    fun removeItem(item: String?) {
        adapter!!.remove(item)
    }

    fun getPosition(): Int {
        return position
    }

    companion object {
        private const val TAG = "SpinnerAdapter"
    }

    init {
        this.list = list
        this.hasHint = hasHint
    }
}

