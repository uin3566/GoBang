package com.xuf.www.gobang;

/**
 * Created by lenov0 on 2015/12/26.
 */
public class CompositionDialog extends BaseButtonDialog {

    public static final String TAG = "CompositionDialog";
    private ButtonClickListener mListener;

    public void setLister(ButtonClickListener lister){
        mListener = lister;
    }

    @Override
    public String getButtonText(int button) {
        String text = null;
        switch (button){
            case BTN_1:
                text = "创建棋局";
                break;
            case BTN_2:
                text = "加入棋局";
                break;
            case BTN_3:
                text = "返 回";
                break;
        }
        return text;
    }

    @Override
    public void onButtonClick(int button) {
        if (mListener != null){
            mListener.onCompositionButtonClick(button);
        }
    }

    public interface ButtonClickListener{
        void onCompositionButtonClick(int button);
    }
}
