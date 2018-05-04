package com.github.jamies1211.banknotes;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

import static com.github.jamies1211.banknotes.Data.Messages.*;

/**
 * Created by Jamie on 03-Aug-16.
 */
public class BankNoteCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		int price = args.<Integer>getOne("price").get();

		if (src instanceof Player) {
			Player player = (Player) src;

			if (price >= 0) {

				if (price >= 25000) {

					EconomyService economyService = BankNotes.getEconomyService();
					Optional<UniqueAccount> optionalAccount = economyService.getOrCreateAccount(player.getUniqueId());

					if (optionalAccount.isPresent()) {

						UniqueAccount account = optionalAccount.get();

						if (account.withdraw(economyService.getDefaultCurrency(), new BigDecimal(price), Cause.of(NamedCause.source(BankNotes.getPlugin()))).getResult() == ResultType.SUCCESS) {

							if (player.getInventory().offer(GenerateBankNote.generateBankNote(price)).getType() == InventoryTransactionResult.Type.SUCCESS) {
								player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + bankNoteCreated
										.replace("%price%", ("$" + new DecimalFormat("#,###,##0.00").format(price).toString()))));
							} else {
								player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + bankNoteNoSpace));
							}
						} else {
							player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + bankNoteNotCreated
									.replace("%price%", ("$" + new DecimalFormat("#,###,##0.00").format(price).toString()))));
						}
					}
				} else {
					player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + bankNoteTooSmall));
				}
			} else {
				player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(prefix + bankNoteNegative));
			}
		} else {
			src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(playerCommand));
		}

		return CommandResult.success();
	}
}

