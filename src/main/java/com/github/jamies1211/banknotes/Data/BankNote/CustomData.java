package com.github.jamies1211.banknotes.Data.BankNote;

import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

/**
 * Created by Jamie on 29/10/2016.
 */
public class CustomData extends AbstractData<CustomData, ImmutableCustomData> {

	// TypeTokens needed for creating Keys (can be created inline too)
	private static TypeToken<Double> TT_Double = new TypeToken<Double>() {};
	private static TypeToken<Value<Double>> TTV_Double = new TypeToken<Value<Double>>() {};

	// Keys for this custom Data
	public static Key<Value<Double>> BANK_NOTE_VALUE = makeSingleKey(TT_Double, TTV_Double, of("Double"), "banknotes:customdata:banknotevalue", "Double");

	// Live Data in this instance
	private Double bankNoteValue;

	// For DataBuilder and personal use
	public CustomData() {
	}

	public CustomData(Double bankNoteValue) {
		this.bankNoteValue = bankNoteValue;
		registerGettersAndSetters();
	}

	@Override
	protected void registerGettersAndSetters() {
		// Getter, Setter and ValueGetter for BANK_NOTE_VALUE
		registerFieldGetter(BANK_NOTE_VALUE, CustomData.this::getBankNoteValue);
		registerFieldSetter(BANK_NOTE_VALUE, CustomData.this::setBankNoteValue);
		registerKeyValue(BANK_NOTE_VALUE, CustomData.this::myDouble);
	}

	// Create immutable version of this
	@Override
	public ImmutableCustomData asImmutable() {
		return new ImmutableCustomData(this.bankNoteValue);
	}

	// Fill Data using DataHolder and MergeFunction
	@Override
	public Optional<CustomData> fill(DataHolder dataHolder, MergeFunction overlap) {
		Optional<Double> bankNoteValue = dataHolder.get(BANK_NOTE_VALUE);
		// Only apply if the custom Data is present
		if (bankNoteValue.isPresent()) {
			CustomData data = this.copy();
			data.bankNoteValue = bankNoteValue.get();

			// merge Data
			data = overlap.merge(this, data);
			if (data != this) {
				this.bankNoteValue = data.bankNoteValue;
			}

			return Optional.of(this);
		}
		return Optional.empty();
	}

	// Fill Data using DataContainer
	@Override
	public Optional<CustomData> from(DataContainer container) {
		Optional<Double> bankNoteValue = container.getDouble(BANK_NOTE_VALUE.getQuery());
		// Only apply if the custom Data is present
		if (bankNoteValue.isPresent()) {
			this.bankNoteValue = bankNoteValue.get();
			return Optional.of(this);
		}
		return Optional.empty();
	}

	// Create copy of this
	@Override
	public CustomData copy() {
		return new CustomData(this.bankNoteValue);
	}

	// Content Version (may be used for updating custom Data later)
	@Override
	public int getContentVersion() {
		return 1;
	}

	// !IMPORTANT! Override toContainer and set your custom Data
	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(BANK_NOTE_VALUE, getBankNoteValue());
	}

	// Getters

	private Double getBankNoteValue() {
		return this.bankNoteValue;
	}

	// Setters

	private void setBankNoteValue(Double bankNoteValue) {
		this.bankNoteValue = bankNoteValue;
	}

	// ValueGetters

	private Value<Double> myDouble() {
		return Sponge.getRegistry().getValueFactory().createValue(BANK_NOTE_VALUE, this.bankNoteValue);
	}
}
