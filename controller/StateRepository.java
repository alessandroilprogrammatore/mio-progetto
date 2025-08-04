package controller;

public interface StateRepository {
    Controller load();
    void save(Controller controller);
}
