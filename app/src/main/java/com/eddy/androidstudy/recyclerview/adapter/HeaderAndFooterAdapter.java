package com.eddy.androidstudy.recyclerview.adapter;

import com.eddy.androidstudy.R;
import com.eddy.androidstudy.recyclerview.data.DataServer;
import com.eddy.androidstudy.recyclerview.entity.Status;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class HeaderAndFooterAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    public HeaderAndFooterAdapter(int dataSize) {
        super(R.layout.item_header_and_footer, DataServer.getSampleData(dataSize));
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
        switch (helper.getLayoutPosition()%
                3){
            case 0:
                helper.setImageResource(R.id.iv,R.mipmap.animation_img1);
                break;
            case 1:
                helper.setImageResource(R.id.iv,R.mipmap.animation_img2);
                break;
            case 2:
                helper.setImageResource(R.id.iv,R.mipmap.animation_img3);
                break;
        }
    }


}
