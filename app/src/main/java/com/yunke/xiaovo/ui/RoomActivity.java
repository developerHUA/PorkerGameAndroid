package com.yunke.xiaovo.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;
import com.yunke.xiaovo.R;
import com.yunke.xiaovo.base.BaseActivity;
import com.yunke.xiaovo.bean.RoomParams;
import com.yunke.xiaovo.bean.RoomResult;
import com.yunke.xiaovo.bean.User;
import com.yunke.xiaovo.manage.UserManager;
import com.yunke.xiaovo.net.HRNetConfig;
import com.yunke.xiaovo.net.HRRequestUtil;
import com.yunke.xiaovo.utils.StringUtil;
import com.yunke.xiaovo.utils.ToastUtils;
import com.yunke.xiaovo.utils.UIHelper;
import com.yunke.xiaovo.widget.CommonButton;
import com.yunke.xiaovo.widget.CropSquareTransformation;
import com.yunke.xiaovo.widget.NumberInputView;

import butterknife.BindView;

/**
 *
 */
public class RoomActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, NumberInputView.OnNumberClickListener {

    @BindView(R.id.btn_create_room)
    CommonButton btnCreateRoom;
    @BindView(R.id.btn_find_room)
    CommonButton btnFindRoom;
    @BindView(R.id.join_view)
    RelativeLayout rlJoinView;
    @BindView(R.id.create_view)
    LinearLayout llCreateView;
    @BindView(R.id.btn_find)
    Button btnFind;
    @BindView(R.id.btn_join)
    CommonButton btnJoin;
    @BindView(R.id.rg_play_rule)
    RadioGroup rgPlayRule;
    @BindView(R.id.rg_play_type)
    RadioGroup rgPlayType;
    @BindView(R.id.btn_confirm)
    CommonButton btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_join_cancel)
    Button btnJoinCancel;
    @BindView(R.id.iv_user_headimg)
    ImageView ivHeadImag;
    @BindView(R.id.tv_nick_name)
    TextView tvNickname;
    @BindView(R.id.tv_user_number)
    TextView tvUserNumber;
    @BindView(R.id.rb_no_remove)
    RadioButton rbNoRemove;
    @BindView(R.id.tv_room_number)
    TextView tvRoomNumber;
    @BindView(R.id.number_view)
    NumberInputView numberView;

    private UserManager mUserManager = UserManager.getInstance();
    private User user = mUserManager.getUser();
    private RoomResult.Result room;
    private StringBuilder roomNumber = new StringBuilder();

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
                hideJoinRoomView();
                break;
            case R.id.btn_find:
                if (TextUtils.isEmpty(roomNumber.toString())) {
                    ToastUtils.showToast("请输入房间号");
                    return;
                }
                btnJoin.setVisibility(View.GONE);
                requestJoinRoom(Integer.parseInt(roomNumber.toString()));
                break;
            case R.id.btn_join_cancel:
                hideJoinRoomView();
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
        rgPlayType.setOnCheckedChangeListener(this);
        numberView.setOnNumberClickListener(this);
    }


    private void hideJoinRoomView() {
        btnJoin.setVisibility(View.GONE);
        if (roomNumber.length() > 0) {
            roomNumber.delete(0, roomNumber.length());
        }
        tvRoomNumber.setText(roomNumber.toString());
        rlJoinView.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();

        Picasso.with(this).load(user.getHeadimgurl()).
                transform(new CropSquareTransformation()).into(ivHeadImag);
        tvNickname.setText(user.getNickname());
        tvUserNumber.setText(getString(R.string.room_user_id, user.getUserId() + ""));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room;
    }


    private void requestJoinRoom(int roomNumber) {
        RoomParams params = new RoomParams(roomNumber, mUserManager.getUserId(),
                mUserManager.getToken());
        HRRequestUtil.postJson(HRNetConfig.JOIN_ROOM_URL, params, joinRoomCallback);
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
        if (ruleTypeId == R.id.rb_no_remove) {
            ruleType = RoomResult.NO_REMOVE;
        } else if (ruleTypeId == R.id.rb_rule_two) {
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_play_three) {
            rbNoRemove.setVisibility(View.VISIBLE);
        } else if (checkedId == R.id.rb_play_four) {
            if (rgPlayRule.getCheckedRadioButtonId() == R.id.rb_no_remove) {
                rgPlayRule.check(R.id.rb_rule_two);
            }
            rbNoRemove.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNumberClick(CommonButton button, int position) {
        if (position == 9) {
            if (roomNumber.length() > 0) {
                roomNumber.delete(0, roomNumber.length());
            }
        } else if (position == 11) {
            if (roomNumber.length() > 0) {
                roomNumber.deleteCharAt(roomNumber.length() - 1);
            }
        } else if (position == 10) {
            roomNumber.append("0");
        } else {
            roomNumber.append(position + 1);
        }
        tvRoomNumber.setText(roomNumber.toString());
        btnJoin.setVisibility(View.GONE);
    }
}
