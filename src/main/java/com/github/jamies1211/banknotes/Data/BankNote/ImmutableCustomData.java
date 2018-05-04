package com.github.jamies1211.banknotes.Data.BankNote;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import static com.github.jamies1211.banknotes.Data.BankNote.CustomData.BANK_NOTE_VALUE;

/**
 * Created by Jamie on 29/10/2016.
 */
public class ImmutableCustomData extends AbstractImmutableData<ImmutableCustomData, CustomData> {

	private Double myDouble;

	public ImmutableCustomData(Double myDouble) {
		this.myDouble = myDouble;
	}

	@Override
	protected void registerGetters() {
		// Getter and ValueGetter for BANK_NOTE_VALUE
		registerFieldGetter(BANK_NOTE_VALUE, this::getMyDouble);
		registerKeyValue(BANK_NOTE_VALUE, this::myDouble);
	}

	// Create mutable version of this
	@Override
	public CustomData asMutable() {
		return new CustomData(this.myDouble);
	}

	// Content Version (may be used for updating custom Data later)
	@Override
	public int getContentVersion() {
		return 1;
	}

	// !IMPORTANT! Override toContainer and set your custom Data
	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(BANK_NOTE_VALUE, getMyDouble());
	}

	// Getters

	private Double getMyDouble() {
		return this.myDouble;
	}

	// Value Getters

	private ImmutableValue<Double> myDouble() {
		return Sponge.getRegistry().getValueFactory().createValue(BANK_NOTE_VALUE, this.myDouble).asImmutable();
	}
}