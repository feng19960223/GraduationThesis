package com.fgr.aabao.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.fgr.aabao.R;

/**
 * 作者：Fgr on 2017/5/8 09:50
 * 邮箱：594878760@qq.com
 * 最新修改日期/修改人员：
 * 说明：自带圆角，支持反转颜色的Button
 * attrs.xml
 * <declare-styleable name="inverseColBtn">
 * <attr name="inverseColor" format="color"/>
 * <attr name="whiteFillFirst" format="boolean"/>
 * </declare-styleable>
 */

public class InverseColorButton extends AppCompatButton {


    public InverseColorButton(Context context) {
        this(context, null);
    }

    public InverseColorButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public InverseColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.inverseColBtn);
        int inverseColor = typedArray.getColor(R.styleable.inverseColBtn_inverseColor, Color.WHITE);
        boolean whiteFillFirst = typedArray.getBoolean(R.styleable.inverseColBtn_whiteFillFirst, true);
        // 同意默认值
        int roundRadius = 10;
        int strokeWidth = 1;
        int strokeColor = inverseColor;// 描边色

        int defaultFillColor = Color.WHITE;
        // 默认状态下的填充色
        GradientDrawable whiteFillGd = new GradientDrawable();
        whiteFillGd.setColor(defaultFillColor);// 填充颜色白
        whiteFillGd.setCornerRadius(roundRadius);// 圆角10
        whiteFillGd.setStroke(strokeWidth, strokeColor);// 描边

        // 按下情况的形态
        int pressedFillColor = inverseColor;
        GradientDrawable inverseGd = new GradientDrawable();
        inverseGd.setColor(pressedFillColor);// 填充颜色白
        inverseGd.setCornerRadius(roundRadius);// 圆角10
        inverseGd.setStroke(strokeWidth, strokeColor);// 描边

        StateListDrawable stateListDrawable = new StateListDrawable();
        int pressed = android.R.attr.state_pressed;
        int windowfocused = android.R.attr.state_window_focused;

        if (whiteFillFirst) {
            stateListDrawable.addState(new int[]{pressed, windowfocused}, inverseGd);
            stateListDrawable.addState(new int[]{-pressed, windowfocused}, whiteFillGd);
        } else {
            stateListDrawable.addState(new int[]{pressed, windowfocused}, whiteFillGd);
            stateListDrawable.addState(new int[]{-pressed, windowfocused}, inverseGd);
        }
        setBackground(stateListDrawable);

        // 字体颜色反转
        int[] unpress = new int[]{-pressed, windowfocused};
        int[] pressing = new int[]{pressed, windowfocused};
        int[][] states = new int[][]{unpress, pressing};
        int[] colors;
        if (whiteFillFirst) {
            colors = new int[]{inverseColor, Color.WHITE};
        } else {
            colors = new int[]{Color.WHITE, inverseColor};
        }
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setTextColor(colorStateList);
    }
}
