package com.hurenkeji.porkergame.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hurenkeji.porkergame.bean.IntentConstants;
import com.hurenkeji.porkergame.bean.RoomResult;
import com.hurenkeji.porkergame.ui.DouDiZhuGameActivity;

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
