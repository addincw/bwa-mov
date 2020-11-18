package com.addincendekia.bwa_mov.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmSeatAdapter
import com.addincendekia.bwa_mov.adapters.TopupOptionAdapter
import com.addincendekia.bwa_mov.models.FilmSeat
import com.addincendekia.bwa_mov.models.TopupOption
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_topup.*

class TopupActivity : AppCompatActivity() {
    var selectedTopupOption = 0
    var selectedPositionTopupOption = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)

        val topupOptions = mutableListOf<TopupOption>(
            TopupOption(10000, false),
            TopupOption(25000, false),
            TopupOption(50000, false),
            TopupOption(75000, false),
            TopupOption(100000, false),
            TopupOption(125000, false),
            TopupOption(150000, false),
            TopupOption(175000, false),
            TopupOption(200000, false)
        )
        val topupOptionAdapter = TopupOptionAdapter(topupOptions)
        topupOptionAdapter.setOnItemClickCallback(object : TopupOptionAdapter.OnItemClickCallback {
            override fun onItemClicked(topupOption: TopupOption, position: Int) {
//                when(selectedTopupOption) {
//                    0 -> btn_topup_now.isEnabled = false
//                    else -> btn_topup_now.isEnabled = true
//                }
//                if(selectedTopupOption != 0 && selectedPositionTopupOption != position){
//                    topupOptions[selectedPositionTopupOption].selected = false
//                    topupOptionAdapter.notifyDataSetChanged()
//                }

                when(topupOption.selected){
                    true -> topupOption.selected = false
                    false -> topupOption.selected = true
                }

                topupOptionAdapter.notifyItemChanged(position)
            }
        })

        rv_topup_option.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        rv_topup_option.adapter = topupOptionAdapter
    }
}