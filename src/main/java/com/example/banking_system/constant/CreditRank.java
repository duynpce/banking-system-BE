package com.example.banking_system.constant;

import java.math.BigDecimal;

public enum CreditRank {
    EXCELLENT{
        @Override
        public BigDecimal getCreditLimit() {
            return BigDecimal.valueOf(10000);
        }
    },
    GOOD{
        @Override
        public BigDecimal getCreditLimit() {
            return BigDecimal.valueOf(1500);
        }
    },
    FAIR{
        @Override
        public BigDecimal getCreditLimit() {
            return BigDecimal.valueOf(500);
        }
    },
    POOR{
        @Override
        public BigDecimal getCreditLimit() {
            return BigDecimal.valueOf(0);
        }
    };

    public abstract BigDecimal getCreditLimit();
}
