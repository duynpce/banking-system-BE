package com.example.banking_system.card.constant;

import com.example.banking_system.account.constant.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public enum CardPrivilege {
    STANDARD{
        @Override
        public BigDecimal getAnnualFee(AccountType holderType) {
            return BigDecimal.valueOf(50).multiply(annualFeeMultiplier(holderType));
        }

        @Override
        public LocalDate getExpirationDate() {
            return LocalDate.now().plusYears(3);
        }

        @Override
        public BigDecimal getCashbackRate(AccountType holderType, CardType cardType) {
            return new BigDecimal("0.005").multiply(cashbackRateMultiplier(holderType, cardType));
        }
    },
    GOLD{
        @Override
        public BigDecimal getAnnualFee(AccountType holderType) {
            return BigDecimal.valueOf(100).multiply(annualFeeMultiplier(holderType));
        }

        @Override
        public LocalDate getExpirationDate() {
            return LocalDate.now().plusYears(4);
        }

        @Override
        public BigDecimal getCashbackRate(AccountType holderType, CardType cardType) {
            return new BigDecimal("0.01").multiply(cashbackRateMultiplier(holderType, cardType));
        }
    },
    PLATINUM{
        @Override
        public BigDecimal getAnnualFee(AccountType holderType) {
            return BigDecimal.valueOf(200).multiply(annualFeeMultiplier(holderType));
        }

        @Override
        public LocalDate getExpirationDate() {
            return LocalDate.now().plusYears(5);
        }

        @Override
        public BigDecimal getCashbackRate(AccountType holderType, CardType cardType) {
            return new BigDecimal("0.015").multiply(cashbackRateMultiplier(holderType, cardType));
        }
    },
    DIAMOND{
        @Override
        public BigDecimal getAnnualFee(AccountType holderType) {
            return BigDecimal.valueOf(500).multiply(annualFeeMultiplier(holderType));
        }

        @Override
        public LocalDate getExpirationDate() {
            return LocalDate.now().plusYears(6);
        }

        @Override
        public BigDecimal getCashbackRate(AccountType holderType, CardType cardType) {
            return new BigDecimal("0.02").multiply(cashbackRateMultiplier(holderType, cardType));
        }
    };

    public abstract BigDecimal getAnnualFee(AccountType holderType);
    public abstract LocalDate getExpirationDate();
    public abstract BigDecimal getCashbackRate(AccountType holderType, CardType cardType);

    // multipliers based on account type
    public BigDecimal annualFeeMultiplier(AccountType holderType) {
        if(holderType == AccountType.PERSONAL){
            return new BigDecimal("1.0");
        } else {
            return new BigDecimal("1.5");
        }
    }
    public BigDecimal cashbackRateMultiplier(AccountType holderType, CardType cardType) {
        BigDecimal cashbackBase;
        if(holderType == AccountType.PERSONAL){
            cashbackBase = new BigDecimal("1.0");
        } else {
            cashbackBase = new BigDecimal("1.3");
        }

        if(cardType == CardType.CREDIT){
            return cashbackBase.multiply(new BigDecimal("1.2"));
        } else {
            return cashbackBase;
        }
    }
}
