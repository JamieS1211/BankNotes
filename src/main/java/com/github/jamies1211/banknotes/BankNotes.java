package com.github.jamies1211.banknotes;

/**
 * Created by Jamie on 07-May-16.
 */

import com.google.inject.Inject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.hanging.ItemFrame;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.github.jamies1211.banknotes.Data.Messages.*;

@Plugin(id = "banknotes", name = "Bank Notes", version = "1.0.0-1.10.2",
		description = "Converts player currency to item currency",
		authors = {"JamieS1211"},
		url = "http://pixelmonweb.officialtjp.com")
public class BankNotes {

	@Inject
	public static BankNotes plugin;

	@Inject
	public static PluginContainer container;

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	public Path getConfigDir() {
		return configDir;
	}

	private static EconomyService economyService;

	public static BankNotes getPlugin() {
		return plugin;
	}

	@Listener
	public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
		if (event.getService().equals(EconomyService.class)) {
			economyService = (EconomyService) event.getNewProviderRegistration().getProvider();
		}
	}

	public static EconomyService getEconomyService() {
		return economyService;
	}

	public static PluginContainer getPluginContainer() {
		return container;
	}

	@Listener
	public void onServerStart(GameInitializationEvent event) {

		plugin = this;
		MessageChannel.TO_CONSOLE.send(TextSerializers.FORMATTING_CODE.deserialize(startup));

		// Register your Data, ImmutableData and DataBuilder in GameInitializationEvent
		/*MyKeys.dummy();
		DataRegistration.builder()
				.dataName("My Standard Data")
				.manipulatorId("standard_data") // prefix is added for you and you can't add it yourself
				.dataClass(BankNoteData.class)
				.immutableClass(ImmutableBankNoteData.class)
				.builder(new BankNoteDataBuilder())
				.buildAndRegister(container);
				*/

		// GenerateBankNote command
		final CommandSpec generateBankNoteCommand = CommandSpec.builder()
				.description(Text.of("Converts money to bank note for easy storage"))
				.extendedDescription(Text.of("Converts money to bank note for easy storage"))
				.arguments(
						GenericArguments.onlyOne(GenericArguments.integer(Text.of("price"))))
				.executor(new BankNoteCommand())
				.build();
		Sponge.getCommandManager().register(this, generateBankNoteCommand, "bank", "banknote");
	}

	@Listener
	public void onEntityInteraction(InteractEntityEvent.Secondary event) {
		if (event.getTargetEntity() instanceof ItemFrame) {
			Optional<Player> causePlayer = event.getCause().first(Player.class);

			if (causePlayer.isPresent()) {
				Player player = causePlayer.get();
				Optional<ItemStack> optionalItem = player.getItemInHand(HandTypes.MAIN_HAND);

				if (optionalItem.isPresent()) {
					ItemStack heldItem = optionalItem.get();

					if (heldItem.getItem().getName().equalsIgnoreCase("minecraft:paper")) {
						if (heldItem.get(BankNoteData.class).isPresent()) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@Listener
	public void onItemInteraction(InteractItemEvent event) {
		Optional<Player> causePlayer = event.getCause().first(Player.class);

		if (causePlayer.isPresent()) {
			Player player = causePlayer.get();

			Sponge.getScheduler().createTaskBuilder().execute(new Runnable() {
				public void run() {
					TokenInteraction.tokenInteraction(player, economyService);
				}
			}).delay(5, TimeUnit.MILLISECONDS).name("TokenInteractionCooldownRemoval:" + player.getUniqueId().toString()).submit(plugin);

		}
	}
}