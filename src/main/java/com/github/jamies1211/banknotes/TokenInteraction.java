package com.github.jamies1211.banknotes;


import com.github.jamies1211.banknotes.Data.BankNote.CustomData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

import static com.github.jamies1211.banknotes.Data.BankNote.CustomData.BANK_NOTE_VALUE;
import static com.github.jamies1211.banknotes.Data.Messages.*;

/**
 * Created by Jamie on 18-Jun-16.
 */
public class TokenInteraction {

	public static void tokenInteraction(Player player, EconomyService economyService) {
		if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
			ItemStack itemInHand = player.getItemInHand(HandTypes.MAIN_HAND).get();
			if (itemInHand.getItem().getName().equalsIgnoreCase("minecraft:paper")) {
				if (itemInHand.get(CustomData.class).isPresent()) {

					Optional<Double> bankNoteValue = itemInHand.get(CustomData.class).get().toContainer().getDouble(BANK_NOTE_VALUE.getQuery());

					if (bankNoteValue.isPresent()) {
						int value = bankNoteValue.get().intValue();

						int endQuantity = itemInHand.getQuantity() - 1;

						itemInHand.setQuantity(endQuantity);

						if (endQuantity < 1) {
							player.setItemInHand(HandTypes.MAIN_HAND, null);
						} else {
							player.setItemInHand(HandTypes.MAIN_HAND, itemInHand);
						}

						UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();

						account.deposit(economyService.getDefaultCurrency(), new BigDecimal(value), Cause.of(NamedCause.source(BankNotes.getPlugin())));

						player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + bankNoteUsed
								.replace("%price%", ("$" + new DecimalFormat("#,###,##0.00").format(value).toString()))));

					}
				}
			}
		}
	}
}


