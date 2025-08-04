package controller;

import java.io.*;

public class FileStateRepository implements StateRepository {
    private static final String PATH = "data/state.dat";

    @Override
    public Controller load() {
        File file = new File(PATH);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Controller) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Controller controller) {
        new File("data").mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH))) {
            out.writeObject(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
