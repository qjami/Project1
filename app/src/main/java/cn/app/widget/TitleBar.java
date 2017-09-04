package cn.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lib.base.EActivity;
import cn.yhub.app.R;

/**
 * Created by li.qing
 * on 2017/6/4.
 */

public class TitleBar extends RelativeLayout
{
    private TextView mRightTv;
    private TextView mCenterTv;
    private TextView mLeftTv;
    private ImageView mRightImg;
    private ImageView mLeftImg;
    private View mViewBg;

    public TitleBar(Context paramContext)
    {
        super(paramContext);
    }

    public TitleBar(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        initTitleBar(paramContext, paramAttributeSet);
    }

    public TitleBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        initTitleBar(paramContext, paramAttributeSet);
    }

    private void initTitleBar(final Context paramContext, AttributeSet paramAttributeSet)
    {
        //inflate(paramContext, R.layout.custom_titlebar_layout, this);
        View contentView = inflate(paramContext, R.layout.custom_titlebar_layout, this);
        this.mLeftImg = ((ImageView)contentView.findViewById(R.id.btn_back));
        this.mViewBg = contentView.findViewById(R.id.title_bg);
        this.mCenterTv = ((TextView)contentView.findViewById(R.id.custom_title_centerTx));
        this.mLeftTv = ((TextView)contentView.findViewById(R.id.custom_title_leftTx));
        this.mRightTv = ((TextView)contentView.findViewById(R.id.custom_title_rightTx));
        this.mRightImg = ((ImageView)contentView.findViewById(R.id.btn_right));
        TypedArray typeArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TitleBar);
        String str1 = typeArray.getString(R.styleable.TitleBar_leftText);
        String str2 = typeArray.getString(R.styleable.TitleBar_rightText);
        String str3 = typeArray.getString(R.styleable.TitleBar_centerText);
        boolean bool = typeArray.getBoolean(R.styleable.TitleBar_isVisBack, true);
        Drawable mRightDrawable = typeArray.getDrawable(R.styleable.TitleBar_rightBg);
        Drawable mLeftDrawable = typeArray.getDrawable(R.styleable.TitleBar_leftBg);
        //localObject1 = typeArray.getDrawable(4);
        this.mLeftTv.setText(str1);
        this.mRightTv.setText(str2);
        this.mCenterTv.setText(str3);
        if (bool && mLeftDrawable == null) {
            mLeftDrawable = ContextCompat.getDrawable(getContext(), R.drawable.bg_back);
        }

        if (mLeftDrawable != null) {
            this.mLeftImg.setImageDrawable(mLeftDrawable);
            this.mLeftImg.setVisibility(VISIBLE);
        }else {
            this.mLeftImg.setVisibility(GONE);
        }
        if (mRightDrawable != null){
            this.mRightImg.setImageDrawable(mRightDrawable);
            this.mRightImg.setVisibility(VISIBLE);
        }else {
            this.mRightImg.setVisibility(GONE);
        }

        if (bool)
        {
            mLeftImg.setVisibility(VISIBLE);
            this.mLeftImg.setOnClickListener(new OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    if ((paramContext instanceof Activity)) {
                        ((EActivity)paramContext).finish();
                    }
                }
            });
        }
        typeArray.recycle();
    }

    public ImageView getLeftImage()
    {
        return this.mLeftImg;
    }

    public TextView getCenterTx()
    {
        return this.mCenterTv;
    }

    public TextView getLeftTx()
    {
        return this.mLeftTv;
    }

    public ImageView getRightImage()
    {
        return this.mRightImg;
    }

    public TextView getRightTx()
    {
        return this.mRightTv;
    }

    public View getTitleBg()
    {
        return this.mViewBg;
    }
}
