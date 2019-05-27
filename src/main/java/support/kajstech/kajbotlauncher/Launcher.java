package support.kajstech.kajbotlauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class Launcher {
    private static Process kajbot = null;

    public static void main(String[] args) throws IOException {
        if (!Files.exists(Paths.get(System.getProperty("user.dir") + "/kajbot.jar")) && !Arrays.toString(args).toLowerCase().contains("-update")) downloadBot(args);
        if (Arrays.toString(args).toLowerCase().contains("-update")) downloadBot(args);
        if (Arrays.toString(args).toLowerCase().contains("-runbot")) runBot();

        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();

            if (line.equalsIgnoreCase("stop") && kajbot != null) {
                LogHelper.info("Stopping bot..");
                if (kajbot.isAlive()) kajbot.destroy();
            }

            if (line.equalsIgnoreCase("update")) {
                if (kajbot != null && kajbot.isAlive()) {
                    LogHelper.info("Stopping bot..");
                    kajbot.destroy();
                }
                downloadBot(args);
            }

            if (line.equalsIgnoreCase("start") && (kajbot == null || !kajbot.isAlive())) {
                LogHelper.info("Starting bot..");
                runBot();
            }

            if (line.equalsIgnoreCase("exit")) {
                LogHelper.info("Exiting..");
                if (kajbot != null && kajbot.isAlive()) kajbot.destroy();
                System.exit(0);
            }
        }
    }

    private static void runBot() throws IOException {
        String[] commands = {"java", "-jar", "kajbot.jar"};
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);
        pb.inheritIO();
        kajbot = pb.start();
    }

    private static void downloadBot(String[] args) throws IOException {
        LogHelper.info("Downloading bot..");
        InputStream downloadUrl = new URL("https://jenkins.jensz12.com/job/Kajbot-Discord/lastSuccessfulBuild/deployedArtifacts/download/artifact.1/").openStream();
        if (Arrays.toString(args).toLowerCase().contains("-dev")) {
            LogHelper.info("[WARNING] Running using '-dev'");
            LogHelper.info("[WARNING] Downloading newest build..");
            downloadUrl = new URL("https://jenkins.jensz12.com/job/Kajbot-Discord-Dev/lastSuccessfulBuild/deployedArtifacts/download/artifact.1/").openStream();
        }
        Files.copy(downloadUrl, Paths.get(System.getProperty("user.dir") + "/kajbot.jar"), StandardCopyOption.REPLACE_EXISTING);
        downloadUrl.close();
        LogHelper.info("Download complete.");
    }
}
