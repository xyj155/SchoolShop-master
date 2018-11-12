package com.example.schoolshop.ui.homefragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.schoolshop.R;
import com.example.schoolshop.base.BaseFragment;
import com.example.schoolshop.ui.LoginActivity;
import com.example.schoolshop.ui.UserAddressListActivity;
import com.example.schoolshop.ui.UserGoodsCollectionActivity;
import com.example.schoolshop.ui.UserInformationActivity;
import com.example.schoolshop.ui.UserOrderListActivity;
import com.example.schoolshop.ui.UserPaymentProcessActivity;
import com.example.schoolshop.ui.UserStoreCollectionActivity;
import com.example.schoolshop.view.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/10/31.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.imageView)
    ImageView imageView;
    @InjectView(R.id.circleImageView)
    CircleImageView circleImageView;
    @InjectView(R.id.tv_payment)
    TextView tvPayment;
    @InjectView(R.id.tv_ticket)
    TextView tvTicket;
    @InjectView(R.id.tv_coin)
    TextView tvCoin;
    @InjectView(R.id.tv_score)
    TextView tvScore;
    @InjectView(R.id.tv_waiting)
    TextView tvWaiting;
    @InjectView(R.id.t_handing)
    TextView tHanding;
    @InjectView(R.id.tv_getting)
    TextView tvGetting;
    @InjectView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @InjectView(R.id.tv_useraddress)
    TextView tvUserAddress;
    @InjectView(R.id.rl_orderlist)
    RelativeLayout rlOrderlist;
    @InjectView(R.id.tv_store_collection)
    TextView tvStoreCollection;
    @InjectView(R.id.tv_goods_collection)
    TextView tvGoodsCollection;
    private TextView tvUsername, ivVip;


    @Override
    protected int setView() {
        return R.layout.fragment_user;
    }

    @Override
    protected void init(View view) {
        ButterKnife.inject(this, view);
        tvUsername = view.findViewById(R.id.tv_username);
        ivVip = view.findViewById(R.id.iv_vip);
        tvUsername.setOnClickListener(this);
        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        if (sp.getString("username", "").isEmpty()){
            tvUsername.setText("登陆/注册");
            ivVip.setVisibility(View.GONE);
        }else {
            ivVip.setVisibility(View.VISIBLE);
            tvUsername.setText(sp.getString("username", ""));
        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_username:
                if (getActivity().getSharedPreferences("user",Context.MODE_PRIVATE).getString("username","").isEmpty()){
                    startActivityForResult(new Intent(getContext(), LoginActivity.class), 0);
                    ivVip.setVisibility(View.GONE);
                }else {
                    ivVip.setVisibility(View.VISIBLE);
                    startActivity(new Intent(getContext(), UserInformationActivity.class));
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null) {
                tvUsername.setVisibility(View.VISIBLE);
                ivVip.setVisibility(View.VISIBLE);
                tvUsername.setText(data.getStringExtra("username"));
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tv_store_collection, R.id.tv_goods_collection, R.id.rl_orderlist, R.id.tv_payment, R.id.tv_ticket, R.id.tv_waiting, R.id.t_handing, R.id.tv_getting, R.id.tv_evaluate, R.id.tv_useraddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_store_collection:
                startActivity(new Intent(getContext(), UserStoreCollectionActivity.class));
                break;
            case R.id.tv_goods_collection:
                startActivity(new Intent(getContext(), UserGoodsCollectionActivity.class));
                break;
            case R.id.rl_orderlist:
                startActivity(new Intent(getContext(), UserOrderListActivity.class));
                break;
            case R.id.tv_useraddress:
                startActivity(new Intent(getContext(), UserAddressListActivity.class));
                break;
            case R.id.tv_payment:

                break;
            case R.id.tv_ticket:
                break;
            case R.id.tv_waiting:
                Intent waitingIntent = new Intent(getContext(), UserPaymentProcessActivity.class);
                waitingIntent.putExtra("index", 0);
                startActivity(waitingIntent);
                break;
            case R.id.t_handing:
                Intent handingIntent = new Intent(getContext(), UserPaymentProcessActivity.class);
                handingIntent.putExtra("index", 1);
                startActivity(handingIntent);
                break;
            case R.id.tv_getting:
                Intent gettingIntent = new Intent(getContext(), UserPaymentProcessActivity.class);
                gettingIntent.putExtra("index", 2);
                startActivity(gettingIntent);
                break;
            case R.id.tv_evaluate:
                Intent evaluateIntent = new Intent(getContext(), UserPaymentProcessActivity.class);
                evaluateIntent.putExtra("index", 3);
                startActivity(evaluateIntent);
                break;
        }
    }
}
