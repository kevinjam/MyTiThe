package com.kevinjanvier.mytithe.tutorial

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.kevinjanvier.mytithe.R
import com.kevinjanvier.mytithe.SplashActivity
import com.kevinjanvier.mytithe.controller.App

class Intro : AppCompatActivity() {
    private lateinit var adapter: MyViewPagerAdapter
    private lateinit var dots: Array<TextView?>
    lateinit var layouts: IntArray
    private var btnSkip: Button? = null
    private var btnNext: Button? = null
    private var dotsLayout: LinearLayout? = null
    private var viewPager: ViewPager? = null


    private val viewpageListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            addBottomDots(position)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_intro)

        viewPager = findViewById(R.id.pager)
        dotsLayout = findViewById(R.id.layoutDots)
        btnSkip = findViewById(R.id.btn_skip)
        btnNext = findViewById(R.id.btn_next)

        layouts = intArrayOf(R.layout.step_one, R.layout.step_two, R.layout.step_tree)
        addBottomDots(0)

        adapter = MyViewPagerAdapter()
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(viewpageListener)

        btnSkip!!.setOnClickListener {
            launchHomeScreen()
        }
       btnNext!!.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                viewPager!!.currentItem = current
            } else {
                launchHomeScreen()
            }
        }


    }


    fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls<TextView>(layouts.size)
        val colorsActive = resources.getColor(R.color.red)
        val colorsInactive = resources.getColor(R.color.colorPrimary)

        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive)
            dotsLayout!!.addView(dots[i])
        }

        if (dots.isNotEmpty())
            dots[currentPage]!!.setTextColor(colorsActive)
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        App.prefs.islogIn = true
        startActivity(Intent(this@Intro, SplashActivity::class.java))
        finish()

    }

    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
}
