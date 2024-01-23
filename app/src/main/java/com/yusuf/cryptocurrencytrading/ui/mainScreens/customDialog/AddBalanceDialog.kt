package com.yusuf.cryptocurrencytrading.ui.mainScreens.customDialog

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

class AddBalanceDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_add_balance, container, false)

        val addEditText: EditText = view.findViewById(R.id.addEditText)
        val addButton: Button = view.findViewById(R.id.addButton)


        addButton.setOnClickListener {
            val amount = addEditText.text.toString().toDoubleOrNull()

            if (amount != null) {
                (targetFragment as WalletFragment).addBalance(amount)
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
}