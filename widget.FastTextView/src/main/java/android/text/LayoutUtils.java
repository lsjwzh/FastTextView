package android.text;

import android.util.Log;

import java.lang.reflect.Field;

public class LayoutUtils {

  static Class sEllipsizerClazz;
  static Field sEllipsizerSourceField;

  static {
    Log.e("LayoutUtils", " static");
    try {
      sEllipsizerClazz = Class.forName("android.text.Layout$Ellipsizer");
      sEllipsizerSourceField = sEllipsizerClazz.getDeclaredField("mText");
      sEllipsizerSourceField.setAccessible(true);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  public static boolean isEllipsizer(CharSequence charSequence) {
    return sEllipsizerClazz != null && sEllipsizerClazz.isInstance(charSequence);

  }

  public static CharSequence extractFromEllipsizer(CharSequence charSequence) {
    if (isEllipsizer(charSequence)) {
      try {
        return (CharSequence) sEllipsizerSourceField.get(charSequence);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
