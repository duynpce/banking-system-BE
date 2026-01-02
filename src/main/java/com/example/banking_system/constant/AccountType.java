package com.example.banking_system.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    PERSONAL{
        @Override
        public boolean canRequestUtilityOrTaxPayments() {
            return false;
        }

        @Override
        public boolean canOpenBusinessCard() {
            return false;
        }

        @Override
        public boolean canOpenPersonalCard() {
            return true;
        }
    },
    BUSINESS{
        @Override
        public boolean canRequestUtilityOrTaxPayments() {
            return false;
        }

        @Override
        public boolean canOpenBusinessCard() {
            return true;
        }

        @Override
        public boolean canOpenPersonalCard() {
            return false;
        }
    },
    GOVERNMENT{
        @Override
        public boolean canRequestUtilityOrTaxPayments() {
            return true;
        }

        @Override
        public boolean canOpenBusinessCard() {
            return false;
        }

        @Override
        public boolean canOpenPersonalCard() {
            return false;
        }
    };


    public abstract boolean canRequestUtilityOrTaxPayments();
    public abstract boolean canOpenBusinessCard();
    public abstract boolean canOpenPersonalCard();

}
