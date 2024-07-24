package org.padlabot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.padlabot.listener.ButtonListener;
import org.padlabot.listener.CommandListener;
import org.padlabot.listener.ModalListener;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Padla {

    public static final String CONFIG_PATH = "src/main/resources/config.json";
    private static final Object[] CLASSES = {
            new ModalListener(), new CommandListener(), new ButtonListener()
    };

    public static void main(String[] args) throws InterruptedException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(" TOKEN ")
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                .setActivity(Activity.playing("Looking for padla`s"))
                .addEventListeners(CLASSES);
пше 
        jdaBuilder.build().awaitReady().updateCommands()
                .addCommands(
                        Commands.slash("setsendchannel", "Setting channel which used for sending requests")
                                .addOption(OptionType.CHANNEL, "channel", "Channel", true),
                        Commands.slash("setreceivechannel", "Setting channel which used for sending requests")
                                .addOption(OptionType.CHANNEL, "channel", "Channel", true),
                        Commands.slash("setupchannels", "Setting message in send channel")
                ).queue();

        configInit();
    }

    private static void configInit() {
        try {
            Files.createFile(Path.of(CONFIG_PATH));
            System.out.println("File created");
        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}