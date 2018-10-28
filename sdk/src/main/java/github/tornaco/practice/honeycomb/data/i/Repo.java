package github.tornaco.practice.honeycomb.data.i;

public interface Repo {
    void reload();

    void reloadAsync();

    void flush();

    void flushAsync();

    String name();
}
