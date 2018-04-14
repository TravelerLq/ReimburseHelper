package cn.unitid.spark.cm.sdk.common;

import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.widget.EditText;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by lyb on 2016/7/1.
 */
public class Utils {
    public static Map<String, String> parseSubject(String subject) {
        Map<String, String> mSubject = new Hashtable<String, String>();
        String[] aSubject = subject.split(",");
        for (String item : aSubject) {
            String[] kv = item.split("=");
            if (kv.length == 2) {
                mSubject.put(kv[0].trim(), kv[1].trim());
            }
        }
        return mSubject;
    }

    public static EditText showPassword(EditText view, boolean isShow) {
        if (isShow) {
            view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        Editable etable = view.getText();
        Selection.setSelection(etable, etable.length());
        return view;
    }
}
