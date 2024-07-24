package org.padlabot.listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();

        if (id.equals("unbanButton")) {
            TextInput linkInput = TextInput.create("linkInput", "Ссылка Steam", TextInputStyle.SHORT)
                    .setRequired(true)
                    .build();
            TextInput cause = TextInput.create("causeInput", "Причина", TextInputStyle.SHORT)
                    .setRequired(false)
                    .build();
            Modal modal = Modal.create("unbanModal", "Заявка")
                    .addActionRow(linkInput)
                    .addActionRow(cause)
                    .build();

            event.replyModal(modal).queue();
        } else if (id.equals("approveBtn")) {

            //TODO: unban
            Message message = event.getMessage();
            String mention = "Принял: <@" + event.getUser().getId() + ">";

            message.editMessageComponents(new ArrayList<>()).queue();

            event.getMessage().addReaction(Emoji.fromCustom("images", 1265617713527656490L, false)).queue();
            event.reply(mention).queue();
        } else if (id.equals("declineBtn")) {
            Message message = event.getMessage();
            String mention = "Отклонил: <@" + event.getUser().getId() + ">";

            message.editMessageComponents(new ArrayList<>()).queue();

            event.getMessage().addReaction(Emoji.fromCustom("download", 1265617684800733219L, false)).queue();
            event.reply(mention).queue();
        }
    }



}
