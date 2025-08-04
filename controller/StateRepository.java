package controller;

public interface StateRepository {
    ControllerState load();
    void save(ControllerState state);
}
