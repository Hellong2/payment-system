package pl.ddconstriction.payment_service.entity;

public enum PaymentStatusEnum {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    EXPIRED("EXPIRED");

    private String value;

    PaymentStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static PaymentStatusEnum fromValue(String value) {
        for (PaymentStatusEnum b : PaymentStatusEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

