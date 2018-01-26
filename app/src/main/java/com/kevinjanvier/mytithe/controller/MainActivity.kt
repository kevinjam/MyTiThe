package com.kevinjanvier.mytithe.controller

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.kevinjanvier.mytithe.custom.MyEditText

import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import com.kevinjanvier.mytithe.R
import com.kevinjanvier.mytithe.utils.AlarmReceiver
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    lateinit var spinner: Spinner
    lateinit var madview: AdView
    lateinit var total_amount: TextView
    lateinit var amount: MyEditText
    lateinit var calculate: Button
    lateinit var mdialog: Dialog
    lateinit var inst: MainActivity
    private lateinit var alarmText:TextView
    var alarmManager: AlarmManager? = null
    var pendingIntent: PendingIntent? = null


    fun setAlarm(message: String) : String{
//        timeAlarm.visibility = View.VISIBLE
//        timeAlarm.text = message
        val msg = message
        log("KKKKKKKKK " + msg)
//        alarmText.text = message
        return msg
    }

//    fun instance(): MainActivity {
//        return inst
//    }


//    fun alarmText():TextView{
//        return timeAlarm
//    }

    public override fun onStart() {
        super.onStart()
//        inst = this
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mdialog = Dialog(this)
        val myIntent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(baseContext, 0, myIntent, 0)

        //check for the currency
        if (App.prefs.myCurrency.isEmpty()) {
            currencyPopup()
        }

        spinner = findViewById(R.id.package_select)
        madview = findViewById(R.id.adView)
        total_amount = findViewById(R.id.total_amount)
        amount = findViewById(R.id.amount_txt)
        calculate = findViewById(R.id.calculate)
        alarmText = findViewById(R.id.timeAlarm)

        alarmText.visibility = View.VISIBLE
        alarmText.text = setAlarm("")
        amount.addTextChangedListener(onTextChangedListener())

        MobileAds.initialize(this@MainActivity, getString(R.string.admob_app_id))

        fab_share.setOnClickListener {
            showPopup()
        }

        bannerAdviews()
        calculateTithe()

        calculate.setOnClickListener{
            if (amount.text.toString().isBlank()) {
                amount.error = "Enter Amount"
            } else {
                val calendar: Calendar = Calendar.getInstance()
                val entered_amount: String = amount.text.toString()
                if (entered_amount.isNotEmpty()) {
                    val everyweek: Long = entered_amount.replace(",", "").toLong()
                    val total = 0.1 * everyweek
                    total_amount.text = convertAmout(total) + " " + App.prefs.myCurrency
                    hideKeyboard()
                }
            }


        }

        val adapter: ArrayAdapter<CharSequence> =
                ArrayAdapter.createFromResource(this, R.array.package_select,
                        android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                log("My Position  " + position)
                when (position) {
                    0 -> {
                        log("Select")
                    }

                    1 -> {
                        hideKeyboard()

                        log("EveryWeek " + position)
                        val calendar: Calendar = Calendar.getInstance()
                        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
                        val monthinYear = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

//                      var amount: String = total_amount.text.toString()
//                      var amount :Long = 500
                        val entered_amount: String = amount.text.toString()
                        if (entered_amount.isNotEmpty()) {
                            val everyweek: Long = entered_amount.replace(",", "").toLong()
                            val total = 0.1 * everyweek
                            total_amount.text = convertAmout(total) + " " + App.prefs.myCurrency

                            log("Each Month ${total.toInt()}")


                            log("Enter Amount $entered_amount")
                            val weeklytotal = (0.1) * entered_amount.replace(",", "").toLong()
                            log("Week :: " + weeklytotal.toString())
                            log("dayoftheWeek " + dayoftheWeek)
//                            total_amount.text = weeklytotal.toString()
                        }

                    }
                    2 -> {
                        hideKeyboard()
                        log("Each  Month")
                        val calendar: Calendar = Calendar.getInstance()
                        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//                      var amount: String = total_amount.text.toString()
//                      var amount :Long = 500
                        val entered_amount: String = amount.text.toString()

                        val weeklytotal = (entered_amount.toLong() * dayoftheWeek) / 0.1
                        log("EachMonth :: " + weeklytotal.toString())
                        log("dayoftheWeek " + dayoftheWeek)

                        val parsed = BigDecimal(weeklytotal).setScale(2, BigDecimal.ROUND_FLOOR).divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
                        val formatt = NumberFormat.getCurrencyInstance().format(parsed)
                        log("FOrmatt $formatt")

                        total_amount.text = convertAmout(weeklytotal) + App.prefs.myCurrency
                    }
                    3 -> {
                        log("One $position")
                        log("Every Year")
                        val calendar: Calendar = Calendar.getInstance()
                        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
//                      var amount: String = total_amount.text.toString()
//                      var amount :Long = 500
                        val entered_amount: String = amount.text.toString()

                        val weeklytotal = (0.1 / dayoftheWeek) * entered_amount.toLong()
                        log("Week :: " + weeklytotal.toString())
                        log("dayoftheWeek " + dayoftheWeek)
                        total_amount.text = convertAmout(weeklytotal) + App.prefs.myCurrency
                    }
                }
            }

        }

    }

    fun convertAmout(amount: Double): String {
        val amountParse = NumberFormat.getInstance()
        val df = amountParse as DecimalFormat
        df.applyPattern("#,###,###,###")
        val msg = df.format(amount)
        return msg
    }

    private fun onTextChangedListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                amount.removeTextChangedListener(this)

                try {
                    var originalString = s.toString()

                    val longval: Long?
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = java.lang.Long.parseLong(originalString)

                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)

                    //setting text after format to EditText
                    amount.setText(formattedString)
                    amount.setSelection(amount.text.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                amount.addTextChangedListener(this)
            }
        }
    }

    fun showPopup() {
        mdialog.setContentView(R.layout.custompopup)
        val txtclose = mdialog.findViewById<TextView>(R.id.txtclose)
        txtclose.text = "X"
        val btnFollow = mdialog.findViewById<Button>(R.id.btnfollow)
        txtclose.setOnClickListener {
            mdialog.dismiss()
        }
        btnFollow.setOnClickListener {
        }
        mdialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mdialog.show()
    }


    fun currencyPopup() {
        mdialog.setContentView(R.layout.currencydialog)
        val spinner = mdialog.findViewById<Spinner>(R.id.Currencyspinner)
        val arraylist = arrayOf("Please Select Currency", "UGX", "EUR", "USD", "AED", "AFN", "ALL", "AUD", "ARS",
                "BAM", "ETB", "NOK", "ILS", "ISK", "SYP", "LYD", "UYU", "YER", "CSD",
                "ETB", "THB", "IDR", "LBP", "AED", "BOB", "QAR", "BHD", "HNL", "HRK",
                "COP", "ALL", "DKK", "MYR", "SEK", "RSD", "BGN", "DOP", "KRW", "LVL",
                "VEF", "CZK", "TND", "KWD", "VND", "JOD", "NZD", "PAB", "CLP", "PEN",
                "GBP", "DZD", "CHF", "RUB", "UAH", "ARS", "SAR", "EGP", "INR", "PYG",
                "TWD", "TRY", "BAM", "OMR", "SGD", "MAD", "BYR", "NIO", "HKD", "LTL")

        val adapter: ArrayAdapter<String>

        val currency: String

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraylist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                log("ItemSelected " + arraylist[position])
                if (arraylist[position] == "Please Select Currency") {
                    //don't save
                } else {
                    App.prefs.myCurrency = arraylist[position]
                    mdialog.dismiss()
                }
            }

        }
        mdialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mdialog.show()


    }

    fun shareIntent(activity: Activity) {
        val shareInt = Intent()
        shareInt.action = Intent.ACTION_SEND
        shareInt.type = "text/plain"
        shareInt.putExtra(Intent.EXTRA_TEXT, getString(R.string.recommend_app) + "\n Download it From Here :"
                + Uri.parse(getString(R.string.playstore_link) + activity.packageName))
        startActivity(Intent.createChooser(shareInt, "send To"))
    }

    private fun bannerAdviews() {
        val ad_request: AdRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        madview.loadAd(ad_request)
    }

    private fun calculateTithe() {
        var amount: Long = 500
        val calendar: Calendar = Calendar.getInstance()
        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
//        val monthinYear = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)


        val mymothly: Long = amount
        val total = 0.1 * mymothly
        log("Each Month $total " + total)

        val weekly: Long = amount
        val weeklytotal = (0.1 / dayoftheWeek) * weekly
        val yeartotal = 1.2 * weekly

        log("Each Week $weeklytotal")

        log("Each Year $yeartotal")


        log("Calendar " + calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH))


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_share) {
            shareIntent(this)
        } else if (id == R.id.action_alarm) {
            println("Alarm")
            setingupAlarm()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Alarm Manager
     */
    @TargetApi(Build.VERSION_CODES.N)
    private fun setingupAlarm() {
        mdialog.setContentView(R.layout.alarm_dialog)
//        var alarmManager: AlarmManager? = null
//        var pendingIntent: PendingIntent? = null
        var alarmTimePicker: TimePicker
        var calendar: Calendar
        alarmTimePicker = mdialog.findViewById<TimePicker>(R.id.alarmTimePicker)
        val tooglebtn = mdialog.findViewById<ToggleButton>(R.id.alarmToggle)



        tooglebtn.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                log(" The toggle is enabled")
                println("Alarm Is On")
                buttonView.text = "TOGGLE ON"

                calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.hour)
                calendar.set(Calendar.MINUTE, alarmTimePicker.minute)
                println("MY HOURS" +Calendar.HOUR_OF_DAY + " alarmTimePicker.hour " + alarmTimePicker.hour)
                println(Calendar.MINUTE  +alarmTimePicker.minute)

                alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager!!.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent)
                mdialog.dismiss()
            }else{
                buttonView.text = "OGGLE OFF"

                log("The toggle is disabled")
                alarmManager?.cancel(pendingIntent)
                //setAlarm("")
                println("Alrm is OFF $pendingIntent")
                println("Alarm Manage " + alarmManager)
            }

        }
        mdialog.show()

    }

    /**
     * Setting up the Alarm
     */


    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    private fun log(msg: String) {
        Log.e(this@MainActivity.javaClass.simpleName, msg)
    }
}
