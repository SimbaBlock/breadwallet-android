package com.breadwallet.presenter.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.breadwallet.model.FeeOption;
import com.breadwallet.repository.FeeRepository;
import com.breadwallet.wallet.wallets.bitcoin.BaseBitcoinWalletManager;

import java.math.BigDecimal;

/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 6/13/18.
 * Copyright (c) 2018 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class SendViewModel extends AndroidViewModel {
    private static final String TAG = SendViewModel.class.getSimpleName();
    private static final String DECIMAL = ".";
    private static final String DECIMAL_WITH_LEADING_ZERO = "0.";

    private String mAddress;
    private String mMemo;
    //Raw amount, ETH, BTC..
    private String mAmount;
    private String mChosenCode;

    public SendViewModel(final Application app) {
        super(app);
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getMemo() {
        return mMemo;
    }

    public void setMemo(String memo) {
        this.mMemo = memo;
    }

    public String getAmount() {
        if (mAmount == null) {
            mAmount = "";
        }
        return mAmount;
    }

    public void setAmount(String amount) {
        if (DECIMAL.equals(amount)) {
            amount = DECIMAL_WITH_LEADING_ZERO;
        }

        this.mAmount = amount;
    }

    public String getChosenCode() {
        return mChosenCode;
    }

    public void setChosenCode(String chosenCode) {
        this.mChosenCode = chosenCode;
    }

    /**
     * Updates the fee option preference in the repository and updates the wallet with the appropriate fee.
     * @param walletManager the wallet manager for which the fee option is being updated
     * @param feeOption the selected fee option
     */
    public void updateFeeOptionPreference(BaseBitcoinWalletManager walletManager, FeeOption feeOption) {
        String currencyCode = walletManager.getCurrencyCode();

        // Set Preferred fee and get fee amount
        FeeRepository.getInstance(getApplication()).putPreferredFeeOptionForCurrency(currencyCode, feeOption);
        BigDecimal fee = FeeRepository.getInstance(getApplication()).getFeeByCurrency(currencyCode, feeOption);

        // Update wallet with preferred fee
        walletManager.getWallet().setFeePerKb(fee.longValue());
    }

    public void clear() {
        mAmount = null;
        mMemo = null;
        mAmount = null;
        mChosenCode = null;
    }
}
