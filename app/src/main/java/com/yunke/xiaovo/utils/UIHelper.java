package com.yunke.xiaovo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yunke.xiaovo.bean.IntentConstants;
import com.yunke.xiaovo.bean.RoomResult;
import com.yunke.xiaovo.ui.DouDiZhuGameActivity;

/**
 *
 */
public class UIHelper {


    public static void showDDZActivity(Context context, RoomResult.Result room) {
        Intent intent = new Intent(context, DouDiZhuGameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstants.ROOM_KEY, room);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


}
