package com.github.jamies1211.banknotes;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

// Example of complex data
// Example of multi-class layout
public class BankNoteData extends AbstractData<BankNoteData, ImmutableBankNoteData> {
    private double bankNoteData;

    BankNoteData(Double bankNoteData) {
        this.bankNoteData = bankNoteData;
        // you must call this!
        registerGettersAndSetters();
    }

    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(MyKeys.BANK_NOTE_VALUE, () -> this.bankNoteData);
        registerFieldSetter(MyKeys.BANK_NOTE_VALUE, id -> this.bankNoteData = bankNoteData);
        registerKeyValue(MyKeys.BANK_NOTE_VALUE, this::bankNoteData);
    }

    public Value<Double> bankNoteData() {
        return Sponge.getRegistry().getValueFactory().createValue(MyKeys.BANK_NOTE_VALUE, bankNoteData);
    }

    @Override
    public Optional<BankNoteData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<BankNoteData> otherData_ = dataHolder.get(BankNoteData.class);
        if (otherData_.isPresent()) {
            BankNoteData otherData = otherData_.get();
            BankNoteData finalData = overlap.merge(this, otherData);
            this.bankNoteData = finalData.bankNoteData;
        }
        return Optional.of(this);
    }

    // the double method isn't strictly necessary but makes implementing the builder easier
    @Override
    public Optional<BankNoteData> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<BankNoteData> from(DataView view) {
        if (view.contains(MyKeys.BANK_NOTE_VALUE.getQuery())) {
            this.bankNoteData = view.getDouble(MyKeys.BANK_NOTE_VALUE.getQuery()).get();
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public BankNoteData copy() {
        return new BankNoteData(this.bankNoteData);
    }

    @Override
    public ImmutableBankNoteData asImmutable() {
        return new ImmutableBankNoteData(this.bankNoteData);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    // IMPORTANT this is what causes your data to be written to NBT
    @Override
    public DataContainer toContainer() {
        return super.toContainer()
                .set(MyKeys.BANK_NOTE_VALUE.getQuery(), this.bankNoteData);
    }
}
