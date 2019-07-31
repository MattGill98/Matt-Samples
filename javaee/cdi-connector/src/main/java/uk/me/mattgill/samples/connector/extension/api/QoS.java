package uk.me.mattgill.samples.connector.extension.api;

public enum QoS {
    
    AT_MOST_ONCE(0), AT_LEAST_ONCE(1), EXACTLY_ONCE(2);

    private int value;

    QoS(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}