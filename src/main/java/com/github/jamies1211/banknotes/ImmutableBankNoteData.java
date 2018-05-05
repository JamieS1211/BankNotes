package com.github.jamies1211.banknotes;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableBankNoteData extends AbstractImmutableData<ImmutableBankNoteData, BankNoteData> {
    private double bankNoteValue;

    public ImmutableBankNoteData(double bankNoteValue) {
        this.bankNoteValue = bankNoteValue;
        registerGetters();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(MyKeys.BANK_NOTE_VALUE, () -> this.bankNoteValue);
        registerKeyValue(MyKeys.BANK_NOTE_VALUE, this::bankNoteValue);
    }

    public ImmutableValue<Double> bankNoteValue() {
        return Sponge.getRegistry().getValueFactory().createValue(MyKeys.BANK_NOTE_VALUE, bankNoteValue).asImmutable();
    }

    @Override
    public BankNoteData asMutable() {
        return new BankNoteData(bankNoteValue);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(MyKeys.BANK_NOTE_VALUE.getQuery(), this.bankNoteValue);
    }
}
