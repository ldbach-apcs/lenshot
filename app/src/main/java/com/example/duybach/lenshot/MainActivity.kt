package com.example.duybach.lenshot

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.duybach.lenshot.MainUtils.MainFragment
import com.example.duybach.lenshot.MainUtils.ProfileFragment

class MainActivity : AppCompatActivity() {

    private val fragments = ArrayList<MainFragment>()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        // Load corresponding fragment here
        when (item.itemId) {
            R.id.navigation_home -> {
                // mTextMessage!!.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                // mTextMessage!!.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                // mTextMessage!!.setText(R.string.title_notifications)
                loadFragment(fragments[0])
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(mainFragment: MainFragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, mainFragment,null)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigationView = findViewById(R.id.navigation) as BottomNavigationView
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fragments.add(ProfileFragment())
    }

    fun signOut() {
        val signInIntent = Intent(this, AuthenticActivity::class.java)

        // Restart user
        val pref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val e = pref.edit()
        e.putString(AuthenticActivity.LOGGED_IN_USER, "null")
        e.apply()

        startActivity(signInIntent)
        finish()
    }
}
