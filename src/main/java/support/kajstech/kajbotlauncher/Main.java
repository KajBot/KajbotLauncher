package support.kajstech.kajbotlauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    private static Process kajbot = null;

    public static void main(String[] args) throws IOException {
        InputStream downloadUrl = new URL("https://jenkins.jensz12.com/job/Kajbot-Discord/lastSuccessfulBuild/deployedArtifacts/download/artifact.1/").openStream();
        if (args[0].equalsIgnoreCase("-dev")) {
            System.out.println("Running in dev mode");
            downloadUrl = new URL("https://jenkins.jensz12.com/job/Kajbot-Discord-Dev/lastSuccessfulBuild/deployedArtifacts/download/artifact.1/").openStream();
        }

        if (!Files.exists(Paths.get(System.getProperty("user.dir") + "/kajbot.jar"))) {
            System.out.println("Downloading bot..");
            Files.copy(downloadUrl, Paths.get(System.getProperty("user.dir") + "/kajbot.jar"), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Download complete.");
        }

        runBot();
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            if (kajbot.isAlive() && line.equalsIgnoreCase("stop")) {
                System.out.println("Stopping bot..");
                kajbot.destroy();
                System.out.println("Bot stopped.");
            }
            if (line.equalsIgnoreCase("update")) {
                System.out.println("Starting update");
                if (kajbot.isAlive()) {
                    System.out.println("Stopping bot");
                    kajbot.destroy();
                }
                System.out.println("Downloading update..");
                Files.copy(downloadUrl, Paths.get(System.getProperty("user.dir") + "/kajbot.jar"), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Update complete.");
                System.out.println("Starting bot.");
                runBot();
            }
            if (line.equalsIgnoreCase("start")) {
                System.out.println("Starting bot.");
                runBot();
                System.out.println("Started bot.");
            }
            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("quit")) {
                System.out.println("Quitting..");
                kajbot.destroy();
                System.out.println("Bot stopped.");
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
}
