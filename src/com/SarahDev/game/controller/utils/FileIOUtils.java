package com.SarahDev.game.controller.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.SarahDev.game.model.DataModel;

public class FileIOUtils {
	private final static String FILENAME = "resources/chineseCheckers.txt";
	private final static String FILENAME_TMP = "resources/ccTmp.txt";

	public static synchronized DataModel readFile() {
		DataModel dataModel = new DataModel();
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FILENAME)))) {
			dataModel = (DataModel) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException ignore) {
		}
		return dataModel;
	}

	public static synchronized void writeFile(final DataModel dataModel) {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(new File(FILENAME_TMP))))) {
			oos.writeObject(dataModel);
			oos.flush();
			oos.close();
			Files.copy(Paths.get(FILENAME_TMP), Paths.get(FILENAME), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ignore) {
		}
	}
}
