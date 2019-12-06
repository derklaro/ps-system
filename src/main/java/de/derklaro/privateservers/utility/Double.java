package de.derklaro.privateservers.utility;

public final class Double<F, S> {

    private F first;

    private S second;

    public Double(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }
}
