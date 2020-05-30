package scheduler.services.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class UserLocalization {

    private Locale locale;
    private ResourceBundle bundle;

    public UserLocalization() {
        locale = Locale.getDefault();
    }

    public Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getBundle() {
        Locale locale = Locale.getDefault();
        return ResourceBundle.getBundle("scheduler.services.localization.All", locale);
    }
}
