package com.example.banking_system.domain.account.constant;

import java.math.BigDecimal;

public enum CreditRank {
    EXCELLENT{
        @Override
        public boolean canOpenCard() {
            return true;
        }

        @Override
        public boolean canBorrowLoan() {
            return true;
        }

        @Override
        public boolean canOpenDeposit() {return true;}
    },
    GOOD{
        @Override
        public boolean canOpenCard() {
            return true;
        }

        @Override
        public boolean canBorrowLoan() {
            return true;
        }

        @Override
        public boolean canOpenDeposit() {return true;}
    },
    FAIR{
        @Override
        public boolean canOpenCard() {
            return true;
        }

        @Override
        public boolean canBorrowLoan() {
            return true;
        }

        @Override
        public boolean canOpenDeposit() {return true;}
    },
    POOR{
        @Override
        public boolean canOpenCard() {
            return false;
        }

        @Override
        public boolean canBorrowLoan() {
            return false;
        }

        @Override
        public boolean canOpenDeposit() {return false;}
    };

//    public abstract BigDecimal getCreditLimit();
    public abstract boolean canBorrowLoan();
    public abstract boolean canOpenCard();
    public abstract boolean canOpenDeposit();
}
