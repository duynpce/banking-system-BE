package com.example.banking_system.constant;

public enum CardType {
    CREDIT{
        @Override
        public boolean balanceCanBeNegative() {
            return true;
        }
    },
    DEBIT{
        @Override
        public boolean balanceCanBeNegative() {
            return false;
        }
    };

    public abstract boolean balanceCanBeNegative();
}
