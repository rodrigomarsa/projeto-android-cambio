package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrybe.currencyview.ui.adapters.CurrencyAdapter
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val apiService = ApiServiceClient.instance

    private val mMenuSelection: AutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val mLoadCurrency: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val mSelectCurrency: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val mWaitingResponse: FrameLayout by lazy { findViewById(R.id.waiting_response_state) }
    private val mCurrencyRatesList: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMenuSelection.setOnItemClickListener { parent, _, position, _ ->
            val currency = parent.getItemAtPosition(position) as String
            mSelectCurrency.visibility = View.GONE
            mWaitingResponse.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    ApiIdlingResource.increment()
                    val rateResponse = apiService.getLatestRates(base = currency)
                    val currencyRates = rateResponse.body()

                    currencyRates?.let {
                        val rates = it.rates
                        val adapter = CurrencyAdapter(rates)
                        withContext(Dispatchers.Main) {
                            mCurrencyRatesList.adapter = adapter
                            mCurrencyRatesList.layoutManager = LinearLayoutManager(baseContext)
                            mCurrencyRatesList.visibility = View.VISIBLE
                            mWaitingResponse.visibility = View.GONE

                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: HttpException) {
                    Log.e("Error", e.message.toString())
                } catch (e: IOException) {
                    Log.e("Error", e.message.toString())
                } finally {
                    ApiIdlingResource.decrement()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        mMenuSelection.visibility = View.VISIBLE
        mLoadCurrency.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                val symbolResponse = apiService.getSymbols()
                val symbols = symbolResponse.body()
                val symbolList = symbols?.symbols?.keys?.toList().orEmpty()
                val adapter =
                    ArrayAdapter(
                        baseContext,
                        android.R.layout.simple_list_item_1,
                        symbolList
                    )
                withContext(Dispatchers.Main) {
                    mMenuSelection.setAdapter(adapter)
                    mLoadCurrency.visibility = View.GONE
                    mSelectCurrency.visibility = View.VISIBLE
                }
            } catch (e: HttpException) {
                Log.e("Error", e.message.toString())
            } catch (e: IOException) {
                Log.e("Error", e.message.toString())
            } finally {
                ApiIdlingResource.decrement()
            }
        }
    }
}
