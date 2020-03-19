package com.breadwallet.presenter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.breadwallet.R;
import com.breadwallet.fch.Cid;
import com.breadwallet.fch.DataCache;
import com.breadwallet.presenter.activities.util.BRActivity;
import com.breadwallet.presenter.entities.TxUiHolder;
import com.breadwallet.ui.wallet.TransactionListAdapter;
import com.breadwallet.wallet.WalletsMaster;
import com.breadwallet.wallet.abstracts.BaseWalletManager;
import com.breadwallet.wallet.wallets.bitcoin.WalletFchManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CidDetailActivity extends BRActivity {

    private BaseWalletManager mWalletManager;
    private DataCache mDataCache;
    private Cid mCid;

    private TextView mName;
    private TextView mBalance;
    private TextView mSend;
    private TextView mSign;
    private RecyclerView mRecyclerView;
    private TransactionListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cid_detail);
        mName = findViewById(R.id.detail_cid);
        mBalance = findViewById(R.id.detail_balance);
        mSend = findViewById(R.id.detail_send);
        mSign = findViewById(R.id.detail_sign);
        mRecyclerView = findViewById(R.id.rv_history);

        mWalletManager = WalletsMaster.getInstance().getCurrentWallet(this);
        mDataCache = mDataCache.getInstance();

        int position = getIntent().getIntExtra("cid_position", 0);
        mCid = mDataCache.getCidList().get(position);

        mName.setText(mCid.getName());
        if (mDataCache.getBalance().containsKey(mCid.getAddress())) {
            BigDecimal b = new BigDecimal(mDataCache.getBalance().get(mCid.getAddress())).divide(WalletFchManager.ONE_FCH_BD);
            mBalance.setText(b.toString());
        } else {
            mBalance.setText("0");
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TransactionListAdapter(this, null, new TransactionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(TxUiHolder item) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        initTxHistory();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CidDetailActivity.this, SendActivity.class);
                i.putExtra("address", mCid.getAddress());
                startActivity(i);
            }
        });
        mSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initTxHistory() {
        List<TxUiHolder> list = new ArrayList<TxUiHolder>();
        for (TxUiHolder tx : mWalletManager.getTxUiHolders(getApplication())) {
            if (tx.getFrom().equalsIgnoreCase(mCid.getAddress()) ||
                    tx.getTo().equalsIgnoreCase(mCid.getAddress())) {
                list.add(tx);
            }
        }
        mAdapter.setItems(list);
        mAdapter.notifyDataSetChanged();
    }

}
