package com.addincendekia.bwa_mov.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.FilmSeatAdapter
import com.addincendekia.bwa_mov.adapters.TopupOptionAdapter
import com.addincendekia.bwa_mov.models.FilmSeat
import com.addincendekia.bwa_mov.models.TopupOption
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_topup.*

class TopupActivity : AppCompatActivity() {
    var selectedTopupOption: Int? = null
    var selectedPositionTopupOption: Int? = null

    lateinit var topupOptionAdapter: TopupOptionAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)

        bindTopupOptions()

        bindViewListener()
    }

    private fun bindViewListener() {
        btn_topup_back.setOnClickListener {
            finish()
        }
        btn_topup_now.setOnClickListener {
            Intent(this, TopupFinishActivity::class.java).also {
                finish()
                startActivity(it)
            }
        }
        field_topup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                if (selectedPositionTopupOption != null) {
                    topupOptions[selectedPositionTopupOption!!].selected = false
                    topupOptionAdapter.notifyItemChanged(selectedPositionTopupOption!!)

                    selectedTopupOption = null
                    selectedPositionTopupOption = null
                }
            }

        })
    }

    private fun bindTopupOptions() {
        topupOptionAdapter = TopupOptionAdapter(topupOptions)

        topupOptionAdapter.setOnItemClickCallback(object : TopupOptionAdapter.OnItemClickCallback {
            override fun onItemClicked(topupOption: TopupOption, position: Int) {
                when (topupOption.selected) {
                    true -> {
                        selectedTopupOption = null
                        selectedPositionTopupOption = null
                        topupOption.selected = false
                    }
                    false -> {
                        if (selectedPositionTopupOption != null && selectedPositionTopupOption != position) {
                            topupOptions[selectedPositionTopupOption!!].selected = false
                            topupOptionAdapter.notifyItemChanged(selectedPositionTopupOption!!)
                        }

                        if (!field_topup.text.isEmpty()) {
                            field_topup.clearFocus()
                            field_topup.text.clear()
                        }

                        selectedTopupOption = topupOption.option!!.toInt()
                        selectedPositionTopupOption = position
                        topupOption.selected = true
                    }
                }

                topupOptionAdapter.notifyItemChanged(position)
            }
        })

        rv_topup_option.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        rv_topup_option.adapter = topupOptionAdapter
    }
}