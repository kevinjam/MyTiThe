package com.kevinjanvier.mytithe

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.kevinjanvier.mytithe.custom.MyEditText

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var spinner : Spinner
    lateinit var madview:AdView
    lateinit var total_amount : TextView
    lateinit var amount: MyEditText
    lateinit var calculate:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        spinner = findViewById(R.id.package_select)
        madview = findViewById(R.id.adView)
        total_amount = findViewById(R.id.total_amount)
        amount = findViewById(R.id.amount_txt)
        calculate = findViewById(R.id.calculate)

        MobileAds.initialize(this@MainActivity, getString(R.string.admob_app_id))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        bannerAdviews()
        calculateTithe()

        calculate.setOnClickListener({
            if (amount.text.toString().isBlank()){
                amount.error = "Enter Amount"
            }else{
                val calendar: Calendar = Calendar.getInstance()
                val entered_amount : String = amount.text.toString()
                if (entered_amount.isNotEmpty()){
                    val everyweek : Long = entered_amount.toLong()
                    val total = 0.1 * everyweek
                    total_amount.text = total.toString()
                }
            }


        })

                val adapter :ArrayAdapter<CharSequence> =
                        ArrayAdapter.createFromResource(this,R.array.package_select,
                                android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                log("nothing")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                log("My Position  " + position)
                when(position){
                    0->{
                        log("Select")
                    }

                    1 ->{
                      log("EveryWeek " +position)
                        val calendar: Calendar = Calendar.getInstance()
                        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
                        val monthinYear = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

//                      var amount: String = total_amount.text.toString()
//                      var amount :Long = 500
                        val entered_amount : String = amount.text.toString()
                        if (entered_amount.isNotEmpty()){
                            val everyweek : Long = entered_amount.toLong()
                            val total = 0.1 * everyweek
                            total_amount.text = total.toString()

                            log("Each Month $total")


                            log("Enter Amount $entered_amount")
//                            val weeklytotal = (0.1 / dayoftheWeek) * entered_amount.toLong()
                            val weeklytotal = (0.1) * entered_amount.toLong()
                            log("Week :: "+weeklytotal.toString())
                            log("dayoftheWeek " +dayoftheWeek)
//                            total_amount.text = weeklytotal.toString()
                        }

                    }
                    2-> {
                        log("Each  Month")
                        val calendar: Calendar = Calendar.getInstance()
                        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//                      var amount: String = total_amount.text.toString()
//                      var amount :Long = 500
                        val entered_amount : String = amount.text.toString()

                        val weeklytotal = (entered_amount.toLong() * dayoftheWeek) / 0.1
                        log("EachMonth :: "+weeklytotal.toString())
                        log("dayoftheWeek " +dayoftheWeek)
                            total_amount.text = weeklytotal.toString()


                    }
                    3->{
                        log("One $position")
                        log("Every Year")
                        val calendar: Calendar = Calendar.getInstance()
                        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
//                      var amount: String = total_amount.text.toString()
//                      var amount :Long = 500
                        val entered_amount : String = amount.text.toString()

                        val weeklytotal = (0.1 / dayoftheWeek) * entered_amount.toLong()
                        log("Week :: "+weeklytotal.toString())
                        log("dayoftheWeek " +dayoftheWeek)
                        total_amount.text = weeklytotal.toString()
                    }
                }


            }

        }




    }

    private fun bannerAdviews() {
        val ad_request : AdRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build()
        madview.loadAd(ad_request)

//
    }

    private fun calculateTithe() {
        var amount :Long = 500
        val calendar: Calendar = Calendar.getInstance()
        val dayoftheWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
//        val monthinYear = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)


        val mymothly : Long = amount
        val total = 0.1 * mymothly
        log("Each Month $total " + total)

        val weekly : Long = amount
        val weeklytotal = (0.1 / dayoftheWeek) * weekly
        val yeartotal = 1.2  * weekly

        log("Each Week $weeklytotal")

        log("Each Year $yeartotal")


        log("Calendar " +calendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH))


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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun log(msg:String){
        Log.e(this@MainActivity.javaClass.simpleName, msg)
    }
}
