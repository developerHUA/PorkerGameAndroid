package com.hurenkeji.porkergame.ui;

import android.view.View;
import android.widget.Button;

import com.hurenkeji.porkergame.R;
import com.hurenkeji.porkergame.base.BaseActivity;
import com.hurenkeji.porkergame.bean.DDZLogicBean;
import com.hurenkeji.porkergame.bean.DDZPorker;
import com.hurenkeji.porkergame.utils.ToastUtils;
import com.hurenkeji.porkergame.widget.DDZPorkerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class DouDiZhuGameActivity extends BaseActivity {

    @BindView(R.id.pv_ddz_view)
    DDZPorkerView porkerView;
    @BindView(R.id.btn_send_porker)
    Button btnSendPorker;
    @BindView(R.id.btn_out_porker)
    Button btnOutPorker;
    @BindView(R.id.pv_ddz_view2)
    DDZPorkerView pvDDZ;
    private List<DDZPorker> porkers = new ArrayList<>();
    List<DDZPorker> selectedPorker = new ArrayList<>();

    @Override
    public void initView() {
        btnSendPorker.setOnClickListener(this);
        btnOutPorker.setOnClickListener(this);
        porkerView.setIsClick(true);
    }


    @Override
    public void initData() {


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_out_porker:
                List<Integer> selectedIndex = porkerView.getClickIndex();
                selectedPorker.clear();
                if (selectedIndex.size() == 0) {
                    ToastUtils.showToast("请选择牌");
                    return;
                }

                for (int i = 0; i < selectedIndex.size(); i++) {

                    selectedPorker.add(porkers.get(selectedIndex.get(i)));
                }


                int[] typeAndSize = DDZLogicBean.getPorkerType(selectedPorker);

                if (typeAndSize[0] == DDZLogicBean.UNKNOWN) {
                    ToastUtils.showToast("牌型不正确");
                    return;
                }

                List<DDZPorker> temp = pvDDZ.getPorkers();
                int[] lastType = DDZLogicBean.getPorkerType(temp);
                if(!DDZLogicBean.comparablePorker(typeAndSize,lastType)) {
                    ToastUtils.showToast("牌不正确");
                    return;
                }


                ToastUtils.showToast(DDZLogicBean.typeToString(typeAndSize));
                for (int i = selectedIndex.size() - 1; i >= 0; i--) {
                    porkers.remove(porkers.get(selectedIndex.get(i)));
                }

                porkerView.clearIndex();
                porkerView.upDatePorker(porkers);
                break;
            case R.id.btn_send_porker:
                List<DDZPorker> allPorker = DDZPorker.getMoveShufflePoker();
                porkers.clear();
//                for (int i = 0; i < (allPorker.size() - 4) / 4 + 4; i++) {
                for (int i = 0; i < (allPorker.size()); i++) {
                    porkers.add(allPorker.get(i));
                }
                Collections.sort(porkers);
                porkerView.upDatePorker(porkers);
                break;



        }
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_ddz_game;
    }
}
