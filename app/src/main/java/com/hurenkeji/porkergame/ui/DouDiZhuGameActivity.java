package com.hurenkeji.porkergame.ui;

import android.view.View;
import android.widget.Button;

import com.hurenkeji.porkergame.R;
import com.hurenkeji.porkergame.base.BaseActivity;
import com.hurenkeji.porkergame.bean.IntentConstants;
import com.hurenkeji.porkergame.bean.RoomResult;
import com.hurenkeji.porkergame.manage.PorkerGameWebSocketManager;
import com.hurenkeji.porkergame.manage.UserManager;
import com.hurenkeji.porkergame.widget.DDZPorkerView;

import butterknife.BindView;

/**
 *
 */
public class DouDiZhuGameActivity extends BaseActivity {

    @BindView(R.id.pv_porker_view)
    DDZPorkerView porkerView;
    @BindView(R.id.btn_out_porker)
    Button btnOutPorker;


    private int roomNumber;
    private int userId;
    private String token;
    private PorkerGameWebSocketManager mSocketManager = PorkerGameWebSocketManager.getInstance();
    private RoomResult room;



    @Override
    public void initView() {
        btnOutPorker.setOnClickListener(this);
        porkerView.setIsClick(true);
    }


    @Override
    public void initData() {
        userId = UserManager.getInstance().getUserId();
        token = UserManager.getInstance().getToken();
        roomNumber = getIntent().getIntExtra(IntentConstants.ROOM_NUMBER_KEY, 0);
        mSocketManager.init(roomNumber, token, userId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_out_porker:

                break;

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ddz_game;
    }





}
