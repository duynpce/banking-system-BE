package com.example.banking_system.constant;

public enum CardStatus {
    ACTIVE{
        @Override
        public boolean canWithdraw() {
            return true;
        }
        @Override
        public boolean canDeposit() {
            return true;
        }
        @Override
        public boolean canTransfer() {
            return true;
        }

        @Override
        public boolean canExtendExpiration() {
            return true;
        }
    },
    // when have a loan overdue or credit card payment overdue
    DISABLE{
        @Override
        public boolean canWithdraw() {
            return false;
        }
        @Override
        public boolean canDeposit() {
            return true;
        }
        @Override
        public boolean canTransfer() {
            return false;
        }
        @Override
        public boolean canExtendExpiration() {
            return false;
        }
    },
    BLOCKED {
        @Override
        public boolean canWithdraw() {
            return false;
        }
        @Override
        public boolean canDeposit() {
            return false;
        }
        @Override
        public boolean canTransfer() {
            return false;
        }

        @Override
        public boolean canExtendExpiration() {
            return false;
        }
    },
    EXPIRED{
        @Override
        public boolean canWithdraw() {
            return false;
        }
        @Override
        public boolean canDeposit() {
            return false;
        }
        @Override
        public boolean canTransfer() {
            return false;
        }

        @Override
        public boolean canExtendExpiration() {
            return true;
        }
    };

    public abstract boolean canWithdraw();
    public abstract boolean canDeposit();
    public abstract boolean canTransfer();
    public abstract boolean canExtendExpiration();
}
