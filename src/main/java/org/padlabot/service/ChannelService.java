package org.padlabot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.padlabot.Padla;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelService { //TODO: improve logging

    private static long sendChannelId;
    private static long receiveChannelId;


    public static void setSendChannelId(long id) {
        sendChannelId = id;
        writeConfigProp("sendChannelId", id);
    }

    public static long getSendChannelId() {
        if (sendChannelId == 0) {
            try {
                sendChannelId = (long) readConfigProp("sendChannelId");
                return sendChannelId;
            } catch (Exception e) {
                e.printStackTrace();
                return 0L;
            }
        }
        return sendChannelId;
    }


    public static void setReceiveChannelId(long id) {
        receiveChannelId = id;
        writeConfigProp("receiveChannelId", id);
    }

    public static long getReceiveChannelId() {
        if (receiveChannelId == 0) {
            try {
                long id = (long) readConfigProp("receiveChannelId");
                receiveChannelId = id;
                return id;
            } catch (Exception e) {
                e.printStackTrace();
                return 0L;
            }
        }
        return receiveChannelId;
    }


    protected static Object readConfigProp(String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(new File(Padla.CONFIG_PATH), Map.class);
            return map.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static void writeConfigProp(String key, Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map;
            try {
                map = objectMapper.readValue(new File(Padla.CONFIG_PATH), Map.class);
            } catch (MismatchedInputException e) {
                map = new HashMap<>();
            }
            map.put(key, value);
            objectMapper.writeValue(new File(Padla.CONFIG_PATH), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setupMessage(TextChannel channel) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Заявка на разбан")
                .build();
        Button button = Button.primary("unbanButton", "Заявка");

        channel.sendMessageEmbeds(embed)
                .setActionRow(button)
                .queue();
    }

    public static String sendUnbanRequest(TextChannel channel, List<ModalMapping> values) {
        String link = values.get(0).getAsString().trim();
        if (verifySteamLink(link)) {
            String cause = values.get(1).getAsString();
            String steamId = extractSteamId(link);

            MessageEmbed embed = new EmbedBuilder()
                    .addField("Steam id:", steamId, false)
                    .addField("Link:", link, false)
                    .addField("Причина:", cause, false)
                    .build();

            Button approveBtn = Button.primary("approveBtn", "Approve").withStyle(ButtonStyle.SUCCESS);
            Button declineBtn = Button.primary("declineBtn", "Decline").withStyle(ButtonStyle.DANGER);

            channel.sendMessageEmbeds(embed).setActionRow(approveBtn, declineBtn).queue();
            return "Success";
        } else {
            return "Steam link is not valid";
        }
    }

    private static String extractSteamId(String steamLink) {
        return steamLink.substring(36, steamLink.length() - 1);
    }

    private static boolean verifySteamLink(String steamLink) {
        Pattern pattern = Pattern.compile("^https://steamcommunity.com/profiles/\\d+/$");
        Matcher matcher = pattern.matcher(steamLink);
        return matcher.find();
    }
}
