package kg.geektech.noactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Prefs {
    private SharedPreferences sharedPreferences;

    public Prefs(Context requireContext) {
        sharedPreferences = requireContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void saveBoardState() {
        sharedPreferences.edit().putBoolean("boardIsShown", true).apply();
    }

    public Boolean isBoardShown() {
        return sharedPreferences.getBoolean("boardIsShown", false);
    }

    public void saveImgUri(Uri uri) {
        sharedPreferences.edit().putString("saveImg", uri.toString()).apply();
    }

    public Uri getUriImg() {
        String text = sharedPreferences.getString("saveImg", null);
        if (text != null) {
            return Uri.parse(text);
        } else return null;
    }

    public void saveMyText(String str) {
        sharedPreferences.edit().putString("saveText", str).apply();
    }

    public String getMyText() {
        return sharedPreferences.getString("saveText", null);
    }

}
