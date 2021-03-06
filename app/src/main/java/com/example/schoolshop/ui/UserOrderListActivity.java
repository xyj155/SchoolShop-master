package com.example.schoolshop.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.schoolshop.R;
import com.example.schoolshop.base.BaseActivity;
import com.example.schoolshop.contract.UserOrderContract;
import com.example.schoolshop.gson.UserOrderFormAllListGson;
import com.example.schoolshop.presenter.UserOrderPresenter;
import com.payelves.sdk.EPay;
import com.payelves.sdk.enums.EPayResult;
import com.payelves.sdk.listener.PayResultListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserOrderListActivity extends BaseActivity implements UserOrderContract.View {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.toolbar)
    RelativeLayout toolbar;
    @InjectView(R.id.sl_order)
    SmartRefreshLayout slOrder;
    @InjectView(R.id.ry_order)
    RecyclerView ryOrder;

    private UserOrderPresenter orderPresenter = new UserOrderPresenter(this);
    private OrderAdapter orderAdapter = new OrderAdapter(null, UserOrderListActivity.this);

    @Override
    public int intiLayout() {
        return R.layout.activity_user_order_list;
    }

    @Override
    public void initView() {
        ButterKnife.inject(this);
        slOrder.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                orderPresenter.getUserOrdersList("1", "");
            }
        });
        orderPresenter.getUserOrdersList("1", "");
        ryOrder.setLayoutManager(new LinearLayoutManager(UserOrderListActivity.this));
        orderAdapter.bindToRecyclerView(ryOrder);
    }

    @Override
    public void initData() {
        initToolBar().setToolbarBackIco().setToolbarSubtitle("全部订单");
    }

    @Override
    protected int setStatusBarColor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }

    @Override
    public void showLoading() {
        showDialog();
    }

    @Override
    public void hideLoading() {
        dialogClose();
    }

    @Override
    public void loadFailed(String msg) {
        slOrder.finishRefresh();
        Toast.makeText(this, "订单拉取错误", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadOrderList(List<UserOrderFormAllListGson> userOrderFormAllListGsons) {
        slOrder.finishRefresh();
        orderAdapter.replaceData(userOrderFormAllListGsons);
        ryOrder.setAdapter(orderAdapter);
    }

    @Override
    public void loadUserCount(List<Integer> list) {

    }

    private  class OrderAdapter extends BaseQuickAdapter<UserOrderFormAllListGson, BaseViewHolder> {
        private Context context;

        public OrderAdapter(@Nullable List<UserOrderFormAllListGson> data, Context context) {
            super(R.layout.ry_user_order_list_item, data);
            this.context = context;
        }

        @Override
        protected void convert(BaseViewHolder helper, final UserOrderFormAllListGson item) {
            final DecimalFormat df = new DecimalFormat("#.00");
            final double v = Double.valueOf(item.getGoods().get(0).getGoods_price()) * item.getGoods().get(0).getNum() + 15;
            helper.setText(R.id.tv_shop_name, item.getShop_name())
                    .setText(R.id.tv_comment, item.getGoods().get(0).getComment() == null ? "" : item.getGoods().get(0).getComment())
                    .setText(R.id.tv_goods_name, item.getGoods().get(0).getGoods_name())
                    .setText(R.id.tv_num, "数量： " + item.getGoods().get(0).getNum() + "")
                    .setText(R.id.tv_price, "￥ " + item.getGoods().get(0).getGoods_price() + "")
                    .setText(R.id.tv_total, "共计：" + df.format(v))
                    .setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setOnClickListener(R.id.tv_pay, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double v1 = Double.valueOf(item.getGoods().get(0).getGoods_price()) * item.getGoods().get(0).getNum() + 15;
                            EPay.getInstance(context).pay("商品购物", "商品", (Double.valueOf(v1 * 100)).intValue(),
                                    "", "", "", new PayResultListener() {

                                        @Override
                                        public void onFinish(Context context, Long payId, String orderId, String payUserId,
                                                             EPayResult payResult, int payType, Integer amount) {
                                            EPay.getInstance(context).closePayView();//关闭快捷支付页面
                                            if (payResult.getCode() == EPayResult.SUCCESS_CODE.getCode()) {
                                                //支付成功逻辑处理
                                                Toast.makeText(context, payResult.getMsg(), Toast.LENGTH_LONG).show();
                                            } else if (payResult.getCode() == EPayResult.FAIL_CODE.getCode()) {
                                                //支付失败逻辑处理
                                                Toast.makeText(context, payResult.getMsg(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
            switch (item.getStatus()) {
                case 0:
                    helper.setText(R.id.tv_status, "订单取消");
                    break;
                case 1:
                    helper.setText(R.id.tv_status, "待付款");
                    break;
                case 2:
                    helper.setText(R.id.tv_status, "等待卖家发货");
                    break;
                case 3:
                    helper.setText(R.id.tv_status, "运输中");
                    break;
                case 4:
                    helper.setText(R.id.tv_status, "已到货");
                    break;
            }
            Glide.with(context).load(item.getShop_cover()).apply(new RequestOptions().transform(new RoundedCorners(7))).into((ImageView) helper.getView(R.id.iv_head));
            Glide.with(context).load(item.getGoods().get(0).getGoods_pic()).apply(new RequestOptions().transform(new RoundedCorners(7))).into((ImageView) helper.getView(R.id.imageView3));
        }
    }
}
