package controller;

import java.io.*;

public class FileStateRepository implements StateRepository {
    private static final String DATA_DIR = "data";
    private static final String STATE_FILE = DATA_DIR + "/state.dat";

    @Override
    public ControllerState load() {
        File file = new File(STATE_FILE);
        if (!file.exists()) {
            return new ControllerState();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (ControllerState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ControllerState();
        }
    }

    @Override
    public void save(ControllerState state) {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(STATE_FILE))) {
            out.writeObject(state);
        } catch (IOException e) {
            // handle exception silently
        }
    }
}
