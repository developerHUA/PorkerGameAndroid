package com.yunke.xiaovo.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.yunke.xiaovo.R;
import com.yunke.xiaovo.base.BaseActivity;
import com.yunke.xiaovo.bean.RoomParams;
import com.yunke.xiaovo.bean.RoomResult;
import com.yunke.xiaovo.manage.UserManager;
import com.yunke.xiaovo.net.HRNetConfig;
import com.yunke.xiaovo.net.HRRequestUtil;
import com.yunke.xiaovo.utils.StringUtil;
import com.yunke.xiaovo.utils.ToastUtils;
import com.yunke.xiaovo.utils.UIHelper;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 *
 */
public class RoomActivity extends BaseActivity {

    @BindView(R.id.btn_create_room)
    Button btnCreateRoom;
    @BindView(R.id.btn_find_room)
    Button btnFindRoom;
    @BindView(R.id.join_view)
    RelativeLayout rlJoinView;
    @BindView(R.id.create_view)
    LinearLayout llCreateView;
    @BindView(R.id.et_room_number)
    EditText etRoomNumber;
    @BindView(R.id.btn_find)
    Button btnFind;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.rg_play_rule)
    RadioGroup rgPlayRule;
    @BindView(R.id.rg_play_type)
    RadioGroup rgPlayType;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_join_cancel)
    Button btnJoinCancel;

    private UserManager mUserManager = UserManager.getInstance();
    private RoomResult.Result room;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_room:
                llCreateView.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_find_room:
                rlJoinView.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_confirm:
                requestCreateRoom();
                break;
            case R.id.btn_cancel:
                llCreateView.setVisibility(View.GONE);
                break;
            case R.id.btn_join:
                UIHelper.showDDZActivity(this, room);
                break;
            case R.id.btn_find:
                String roomNumber = etRoomNumber.getText().toString().trim();
                if (TextUtils.isEmpty(roomNumber)) {
                    ToastUtils.showToast("请输入房间号");
                    return;
                }
                btnJoin.setVisibility(View.GONE);
                requestJoinRoom(Integer.parseInt(roomNumber));
                break;
            case R.id.btn_join_cancel:
                rlJoinView.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void initView() {
        super.initView();
        btnCreateRoom.setOnClickListener(this);
        btnFindRoom.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
        btnJoinCancel.setOnClickListener(this);
    }


    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room;
    }


    private void requestJoinRoom(int roomNumber) {
        RoomParams params = new RoomParams(roomNumber, mUserManager.getUserId(),
                mUserManager.getToken());
        HRRequestUtil.postJson(HRNetConfig.CREATE_ROOM_URL, params, joinRoomCallback);
    }


    private StringCallback joinRoomCallback = new StringCallback() {

        @Override
        public void onSuccess(Response<String> response) {
            RoomResult roomResult = StringUtil.jsonToObject(response.body(), RoomResult.class);
            if (roomResult != null && roomResult.result != null) {
                btnJoin.setVisibility(View.VISIBLE);
                room = roomResult.result;
            } else {
                ToastUtils.showToast("未找到房间");
            }

        }

        @Override
        public void onError(Response<String> response) {
            super.onError(response);
            ToastUtils.showToast("请求失败");

        }
    };


    private void requestCreateRoom() {
        int typeId = rgPlayType.getCheckedRadioButtonId();
        int ruleTypeId = rgPlayRule.getCheckedRadioButtonId();
        int type;
        int ruleType;
        if (typeId == R.id.rb_play_three) {
            type = RoomResult.D_D_Z_THREE_TYPE;
        } else {
            type = RoomResult.D_D_Z_FOUR_TYPE;
        }
        if (ruleTypeId == R.id.rb_rule_two) {
            ruleType = RoomResult.REMOVE_DOUBLE_TWO;
        } else {
            ruleType = RoomResult.REMOVE_DOUBLE_THREE;
        }

        RoomParams params = new RoomParams(mUserManager.getUserId(),
                mUserManager.getToken(), type, ruleType);
        HRRequestUtil.postJson(HRNetConfig.CREATE_ROOM_URL, params, createRoomCallback);
    }


    private StringCallback createRoomCallback = new StringCallback() {

        @Override
        public void onSuccess(Response<String> response) {
            RoomResult roomResult = StringUtil.jsonToObject(response.body(), RoomResult.class);
            if (roomResult != null && roomResult.result != null) {
                UIHelper.showDDZActivity(RoomActivity.this, roomResult.result);
            }

        }

        @Override
        public void onError(Response<String> response) {
            super.onError(response);
            ToastUtils.showToast("请求失败");

        }
    };

}
