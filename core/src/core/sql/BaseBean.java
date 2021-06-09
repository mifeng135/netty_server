package core.sql;

public abstract class BaseBean<T> {
    public abstract T getId();

    public abstract void setId(T value);
}
