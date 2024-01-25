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
import com.yusuf.cryptocurrencytrading.ui.mainScreens.WalletFragment

class CheckBalanceDialog : DialogFragment() {

    private var onDismissListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_check_balance, container, false)

        val checkEditText: EditText = view.findViewById(R.id.checkEditText)
        val checkButton: Button = view.findViewById(R.id.checkButton)


        checkButton.setOnClickListener {
            val amount = checkEditText.text.toString().toDoubleOrNull()

            if (amount != null) {
                (targetFragment as WalletFragment).checkBalance(amount)
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