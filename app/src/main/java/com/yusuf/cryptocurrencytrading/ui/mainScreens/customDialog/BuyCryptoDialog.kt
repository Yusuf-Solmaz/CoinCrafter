package com.yusuf.cryptocurrencytrading.ui.mainScreens.customDialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.ui.mainScreens.CoinDetailFragment


class BuyCryptoDialog : DialogFragment() {

    private var onDismissListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_buy_crypto, container, false)

        val addEditText: EditText = view.findViewById(R.id.addEditText)
        val addButton: Button = view.findViewById(R.id.addButton)


        addButton.setOnClickListener {
            val amount = addEditText.text.toString().toDoubleOrNull()

            if (amount != null) {
                (targetFragment as CoinDetailFragment).buyCrypto(amount)
                dismiss()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()


        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog?.window?.setGravity(Gravity.CENTER)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()
    }

    fun setOnDismissListener(listener: () -> Unit) {
        this.onDismissListener = listener
    }

}