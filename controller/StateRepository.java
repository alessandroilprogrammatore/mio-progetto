// File: StateRepository.java
package controller;

/**
 * Interfaccia per la persistenza dello stato del Controller.
 */
public interface StateRepository {
    ControllerState load();
    void save(ControllerState state);
}
