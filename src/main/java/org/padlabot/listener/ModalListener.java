package org.padlabot.listener;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.padlabot.service.ChannelService;

import java.util.List;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String id = event.getModalId();
        List<ModalMapping> values = event.getValues();

        if (id.equals("unbanModal")) {
            TextChannel receiveChannel = event.getJDA().getTextChannelById(ChannelService.getReceiveChannelId());
            event.reply(ChannelService.sendUnbanRequest(receiveChannel, values)).setEphemeral(true).queue();
        }

    }



}
