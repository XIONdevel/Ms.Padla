package org.padlabot.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.padlabot.service.ChannelService;

public class CommandListener extends ListenerAdapter {

    private SlashCommandInteractionEvent event;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        this.event = event;
        String eventName = event.getName();

        if (eventName.equals("setreceivechannel")) {
            try {
                ChannelService.setReceiveChannelId(Long.parseLong(event.getOption("channel").getAsString()));
                success();
            } catch (Exception e) {
                e.printStackTrace();
                error();
            }
        } else if (eventName.equals("setsendchannel")) {
            try {
                ChannelService.setSendChannelId(Long.parseLong(event.getOption("channel").getAsString()));
                success();
            } catch (Exception e) {
                e.printStackTrace();
                error();
            }
        } else if (eventName.equals("setupchannels")) {
            try {
                JDA jda = event.getJDA();
                long sendId = ChannelService.getSendChannelId();
                if (sendId == 0) {
                    event.reply("Id does not set").setEphemeral(true).queue();
                    return;
                }
                ChannelService.setupMessage(jda.getTextChannelById(sendId));
                success();
            } catch (Exception e) {
                e.printStackTrace();
                error();
            }
        }


    }

    private void success() {
        this.event.reply("Success").setEphemeral(true).queue();
    }

    private void error() {
        this.event.reply("Encountered an error").setEphemeral(true).queue();
    }


}
