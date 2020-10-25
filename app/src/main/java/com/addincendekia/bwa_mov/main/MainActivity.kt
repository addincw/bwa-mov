package com.addincendekia.bwa_mov.main

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.main.fragments.MovieFragment

import com.addincendekia.bwa_mov.main.fragments.ProfileFragment
import com.addincendekia.bwa_mov.main.fragments.TicketFragment
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var fragmentMovie = MovieFragment()
        var fragmentTicket = TicketFragment()
        var fragmentProfile = ProfileFragment()

        _setFragment(fragmentMovie)
        _changeMenuState(iv_menu_menu, tv_menu_menu, R.drawable.menu_active, R.color.colorAppPrimary)

        menu_menu.setOnClickListener{
            _setFragment(fragmentMovie)

            _changeMenuState(iv_menu_menu, tv_menu_menu, R.drawable.menu_active, R.color.colorAppPrimary)
            _changeMenuState(iv_menu_ticket, tv_menu_ticket, R.drawable.ticket, R.color.colorAppBase)
            _changeMenuState(iv_menu_profile, tv_menu_profile, R.drawable.profile, R.color.colorAppBase)
        }
        menu_ticket.setOnClickListener{
            _setFragment(fragmentTicket)

            _changeMenuState(iv_menu_ticket, tv_menu_ticket, R.drawable.ticket_active, R.color.colorAppPrimary)
            _changeMenuState(iv_menu_menu, tv_menu_menu, R.drawable.menu, R.color.colorAppBase)
            _changeMenuState(iv_menu_profile, tv_menu_profile, R.drawable.profile, R.color.colorAppBase)
        }
        menu_profile.setOnClickListener{
            _setFragment(fragmentProfile)

            _changeMenuState(iv_menu_profile, tv_menu_profile, R.drawable.profile_active, R.color.colorAppPrimary)
            _changeMenuState(iv_menu_menu, tv_menu_menu, R.drawable.menu, R.color.colorAppBase)
            _changeMenuState(iv_menu_ticket, tv_menu_ticket, R.drawable.ticket, R.color.colorAppBase)
        }
    }

    private fun _changeMenuState(imageView: ImageView, textView: TextView, idIcon: Int, idColor: Int) {
        imageView.setImageResource(idIcon)
        textView.setTextColor(ContextCompat.getColor(this, idColor))
        if (idColor == R.color.colorAppPrimary) {
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD)
        }else{
            textView.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun _setFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.layout_frame, fragment)
        fragmentTransaction.commit()
    }
}
