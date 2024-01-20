package com.yusuf.cryptocurrencytrading.ui.mainScreens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.FragmentCoinDetailBinding

class CoinDetailFragment : Fragment() {

    private lateinit var binding : FragmentCoinDetailBinding

    private val bundle : CoinDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinDetailBinding.inflate(inflater,container,false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coin = bundle.coin!!
        Log.i("coinDetailCoin",coin.name)

        loadWebViewChart(coin)
        setButtonOnClick(coin)
        handleViews(coin)

    }
    private fun setButtonOnClick(coin: CryptoCurrency) {
        val oneMonth = binding.button1M
        val oneWeek = binding.button1W
        val oneDay = binding.button1D
        val oneHour = binding.button1H
        val fourHour = binding.button4H
        val fifteenMinute = binding.button15Min

        val clickListener = View.OnClickListener {
            when (it.id) {
                fifteenMinute.id -> loadWebViewChartData(
                    it,
                    "15",
                    coin,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    oneHour
                )

                fourHour.id -> loadWebViewChartData(
                    it,
                    "4H",
                    coin,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fifteenMinute,
                    oneHour
                )

                oneHour.id -> loadWebViewChartData(
                    it,
                    "1H",
                    coin,
                    oneDay,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    fifteenMinute
                )

                oneDay.id -> loadWebViewChartData(
                    it,
                    "D",
                    coin,
                    fifteenMinute,
                    oneMonth,
                    oneWeek,
                    fourHour,
                    oneHour
                )

                oneWeek.id -> loadWebViewChartData(
                    it,
                    "W",
                    coin,
                    oneDay,
                    oneMonth,
                    fifteenMinute,
                    fourHour,
                    oneHour
                )

                oneMonth.id -> loadWebViewChartData(
                    it,
                    "M",
                    coin,
                    oneDay,
                    fifteenMinute,
                    oneWeek,
                    fourHour,
                    oneHour
                )


            }
        }
        fifteenMinute.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        oneMonth.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
    }

    private fun loadWebViewChartData(
        it: View?,
        s: String,
        coin: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {

        disableButton(oneDay,oneMonth,oneWeek,fourHour,oneHour)
        it!!.setBackgroundResource(R.drawable.time_button)
        binding.coinWebView.settings.javaScriptEnabled=true
        binding.coinWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        val url = "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" +
                coin.symbol.toString() + "USD&interval=" + s.toString() +
                "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6" +
                "&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}" +
                "&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com" +
                "&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

        binding.coinWebView.loadUrl(url)
    }

    private fun disableButton(oneDay: AppCompatButton, oneMonth: AppCompatButton, oneWeek: AppCompatButton, fourHour: AppCompatButton, oneHour: AppCompatButton) {

        oneDay.background = null
        oneMonth.background = null
        oneWeek.background = null
        fourHour.background = null
        oneHour.background = null

    }

    private fun loadWebViewChart(coin: CryptoCurrency){
        binding.coinWebView.settings.javaScriptEnabled=true
        binding.coinWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)

        binding.coinWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + coin.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun handleViews(coin: CryptoCurrency) {
        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png"
        ).placeholder(R.drawable.baseline_currency_bitcoin_24).into(binding.coinImageView)

        binding.coinName.text = coin.name

        binding.coinPrice.text = "${String.format("%.2f",coin.quotes[0].price)}$"

        if (0 > coin.quotes[0].percentChange24h){
            binding.coinRatio.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            binding.coinRatio.text = "${String.format("%.02f",coin.quotes[0].percentChange24h)}%"
        } else {
            binding.coinRatio.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            binding.coinRatio.text = "${String.format("%.02f",coin.quotes[0].percentChange24h)}%"
        }


        binding.backImageButton.setOnClickListener {
            findNavController().navigate(
                CoinDetailFragmentDirections.actionCoinDetailFragmentToMainCryptoFragment2()
            )
        }
    }



}