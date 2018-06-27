package android.text;

import android.util.Log;

public class LayoutUtils {

  static Class sEllipsizerClazz;

  static {
    Log.e("LayoutUtils", " static");
    try {
      sEllipsizerClazz = Class.forName("android.text.Layout$Ellipsizer");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static boolean isEllipsizer(CharSequence charSequence) {
    return sEllipsizerClazz != null && sEllipsizerClazz.isInstance(charSequence);

  }
}
