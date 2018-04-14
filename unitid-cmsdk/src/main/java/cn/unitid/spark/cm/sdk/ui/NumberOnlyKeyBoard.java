package cn.unitid.spark.cm.sdk.ui;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import cn.unitid.spark.cm.sdk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字输入
 */
public class NumberOnlyKeyBoard {
    private Context ctx;
    private KeyboardView keyboardView;

    private EditText ed;

    public NumberOnlyKeyBoard(View parent, Context ctx) {
        this.ctx = ctx;
        Keyboard k1 = new Keyboard(ctx, R.xml.numonly);
        String num = "0123456789";
        List<String> nums = new ArrayList<String>();
        for (int i = 0; i < num.length(); i++) {
            nums.add(num.charAt(i) + "");
        }
        List<Keyboard.Key> keynum = k1.getKeys();
        for (Keyboard.Key key : keynum) {
            if (key.label != null && num.contains(key.label.toString())) {
                int j = Math.abs((int) (Math.random() * nums.size()));
                key.label = nums.get(j);
                key.codes[0] = nums.get(j).charAt(0);
                nums.remove(j);
            }
        }
        keyboardView = (KeyboardView) parent.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (primaryCode == 57419) { // go left
                if (start > 0) {
                    ed.setSelection(start - 1);
                }
            } else if (primaryCode == 57421) { // go right
                if (start < ed.length()) {
                    ed.setSelection(start + 1);
                }
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void showKeyboard(EditText ed) {
        this.ed = ed;
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        ed = null;
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }
}