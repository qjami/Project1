package cn.app.viewholder;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import lib.imageloader.GlideFactory;
import cn.yhub.app.R;
import cn.app.model.ZHPackageBean;

/**
 *
 */
public class PackagesViewHolder extends BaseViewHolder<ZHPackageBean> {
    private TextView tVpname;
    private TextView tVsy;
    private TextView tVyy;
    private ImageView testImg;


    public PackagesViewHolder(ViewGroup parent) {
        super(parent, R.layout.view_package_item);
        tVpname = $(R.id.tv_pnama);
        tVsy = $(R.id.tv_sy);
        tVyy = $(R.id.tv_yy);
    }

    @Override
    public void setData(final ZHPackageBean zhPackageBean){
        Log.i("ViewHolder","position"+getDataPosition());
        tVpname.setText(zhPackageBean.getPackageName());
        tVsy.setText(String.valueOf(zhPackageBean.getTotalCredit() - zhPackageBean.getUsedCredit()));
        tVyy.setText(String.valueOf(zhPackageBean.getUsedCredit()));
        if (null != testImg)
            GlideFactory.getLoader().loadImageView("url", testImg);
        /*Glide.with(getContext())
                .load(zhPackageBean.getFace())
                .placeholder(R.drawable.default_image)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mImg_face);*/
    }
}
