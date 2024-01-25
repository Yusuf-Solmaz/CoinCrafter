import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.yusuf.cryptocurrencytrading.R

class SellCryptoDialog(context: Context) : Dialog(context) {
    private var onSellClickListener: OnSellClickListener? = null

    private var onSellAllClickListener: OnSellAllClickListener? = null

    interface OnSellClickListener {
        fun onSellClick(sellAmount: Double)
    }
    interface OnSellAllClickListener {
        fun onSellAllClick()
    }

    fun setOnSellClickListener(listener: OnSellClickListener,listenerAll: OnSellAllClickListener) {
        onSellClickListener = listener
        onSellAllClickListener = listenerAll

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sell_crypto)

        val sellAllButton: Button = findViewById(R.id.sellAllButton)
        val sellButton: Button = findViewById(R.id.sellButton)
        val sellAmountEditText: EditText = findViewById(R.id.sellEditText)

        sellButton.setOnClickListener {
            val sellAmountText = sellAmountEditText.text.toString()
            if (sellAmountText.isNotEmpty()) {
                val sellAmount = sellAmountText.toDouble()
                onSellClickListener?.onSellClick(sellAmount)
                dismiss()
            } else {
                Toast.makeText(context, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
            }
        }

        sellAllButton.setOnClickListener {

                onSellAllClickListener?.onSellAllClick()
                dismiss()
            }
        }
    }
