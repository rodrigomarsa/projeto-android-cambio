package com.betrybe.currencyview.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView

class CurrencyAdapter(private val currencies: Map<String, Double>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name: MaterialTextView
        val rate: MaterialTextView

        init {
            name = view.findViewById(R.id.item_currency_name)
            rate = view.findViewById(R.id.item_currency_rate)
        }
    }

    override fun getItemCount(): Int = currencies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies.keys.toList()[position]
        val rate = currencies.values.toList()[position]
        holder.name.text = currency
        holder.rate.text = rate.toString()
    }
}