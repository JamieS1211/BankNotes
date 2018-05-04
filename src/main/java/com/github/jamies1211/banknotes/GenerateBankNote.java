package com.github.jamies1211.banknotes;

import com.github.jamies1211.banknotes.Data.BankNote.CustomData;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Jamie on 03-Aug-16.
 */
public class GenerateBankNote {

	public static ItemStack generateBankNote(int price) {

		ItemStack bankNote = ItemStack.builder().itemType(ItemTypes.PAPER).build();
		bankNote.offer(Keys.DISPLAY_NAME, Text.of(TextColors.BLUE, "$%price% Bank Note".replace("%price%", Integer.toString(price))));
		bankNote.offer(new CustomData((double) price));

		return bankNote;
	}
}
