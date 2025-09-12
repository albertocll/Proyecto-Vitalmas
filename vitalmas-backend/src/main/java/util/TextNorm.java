package util;

import java.text.Normalizer;
import java.util.Locale;

public final class TextNorm {
    private TextNorm() { }

    public static String normKey(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        t = t.toLowerCase(Locale.ROOT).trim();
        return t.replaceAll("\\s+", " ");
    }
}
