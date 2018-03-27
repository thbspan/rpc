package com.github.thbspan.rpc.invoker;

import java.io.Serializable;
import java.util.Objects;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private Object value;
    private Exception exception;
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    public Exception getException() {
        return exception;
    }
    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return Objects.equals(value, result.value) &&
                Objects.equals(exception, result.exception);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, exception);
    }

    @Override
    public String toString() {
        return "Result{" +
                "value=" + value +
                ", exception=" + exception +
                '}';
    }
}
