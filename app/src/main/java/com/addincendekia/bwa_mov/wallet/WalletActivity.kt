package com.addincendekia.bwa_mov.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.addincendekia.bwa_mov.R
import com.addincendekia.bwa_mov.adapters.TransactionHistoryAdapter
import com.addincendekia.bwa_mov.models.TransactionHistory
import com.addincendekia.bwa_mov.utils.Currency
import com.addincendekia.bwa_mov.utils.UserPreferences
import kotlinx.android.synthetic.main.activity_wallet.*

class WalletActivity : AppCompatActivity() {
    private lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)

        userPref = UserPreferences(this)

        tv_wallet_amount.text = "IDR " + Currency(userPref.getValue("saldo")?.toDouble()).toRupiah()

        val transactionHistories = setOf<TransactionHistory>(
            TransactionHistory("Avenger Infinity War", "action, sci-fi", 50000, 1),
            TransactionHistory("Frozen", "cartoon, drama", 50000, 1),
            TransactionHistory("Topup", "topup via mobile banking", 1300000, 0),
            TransactionHistory("Midway Midway Midway", "action", 50000, 1)
        )
        val decorator = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.recycleview_devider
            )!!
        )
        rv_wallet_history.layoutManager = LinearLayoutManager(this)
        rv_wallet_history.adapter = TransactionHistoryAdapter(transactionHistories)
        rv_wallet_history.addItemDecoration(decorator)

        btn_wallet_back.setOnClickListener {
            finish()
        }
    }
}