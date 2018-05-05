package com.github.jamies1211.banknotes;

import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class BankNoteDataBuilder extends AbstractDataBuilder<BankNoteData> implements DataManipulatorBuilder<BankNoteData, ImmutableBankNoteData> {
    BankNoteDataBuilder() {
        super(BankNoteData.class, 1);
    }

    @Override
    public BankNoteData create() {
        return new BankNoteData(0.0);
    }

    @Override
    public Optional<BankNoteData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<BankNoteData> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
    }
}
