package com.number26.transactions;

/**
 * Created by kmg on 2016-01-01.
 */
public class Transaction {


    private double amount;
    private String type;
    private Long parentId;


    public Transaction() {
    }

    public Transaction(double amount, String type, Long parentId) {
        this.amount = amount;
        this.type = type;
        this.parentId = parentId;
    }

    private Transaction(Builder builder) {
        setAmount(builder.amount);
        setType(builder.type);
        setParentId(builder.parentId);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", parentId=" + parentId +
                '}';
    }

    public static final class Builder {
        private double amount;
        private String type;
        private Long parentId;

        private Builder() {
        }

        public Builder amount(double val) {
            amount = val;
            return this;
        }

        public Builder type(String val) {
            type = val;
            return this;
        }

        public Builder parentId(Long val) {
            parentId = val;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
