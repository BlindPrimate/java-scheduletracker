package scheduler.services;

import scheduler.services.localization.TimeUtil;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoggerUtil {

    // get current working path
    private Path path = Paths.get(System.getProperty("user.dir"),"src/scheduler/services/logs/logins.txt");
    private File loginLog = path.toFile();

    public LoggerUtil() {
    }

    public void logon() throws Exception {
        if (!loginLog.exists()) {
            loginLog.createNewFile();
        }
        Authenticator auth = Authenticator.getInstance();
        FileWriter printer = new FileWriter(loginLog, true);
        String line = "Time: " + TimeUtil.getUTC() + " User:" + auth.getUsername() + "\n";
        printer.write(line);
        printer.close();
    }

}
