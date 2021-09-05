package com.ventus.core.util;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

public class DiscordAPI {
    private WebhookClient client;
    private WebhookClient client_logs = WebhookClient.withUrl("https://discord.com/api/webhooks/864911298210824254/atfebpTjEnR5qFSSyHftYRtrqFl3AApjf9b7MAKkNElKos-mRU0X3D9vwLEQydMhsLXD");
    public DiscordAPI(String url){
        client = WebhookClient.withUrl(url);
    }

    public void sendMessage(String name, String mes){
        // Send and log (using embed)
        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(0xFF00EE)
                .setDescription(mes)
                .build();
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(name); // use this username
//        builder.setAvatarUrl(avatarUrl); // use this avatar
//        builder.setContent(mes);
        builder.addEmbeds(embed);
        client.send(builder.build());
        client_logs.send(builder.build());
    }
}
