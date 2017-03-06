package com.bitsinharmony.recognito.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bitsinharmony.recognito.Recognito;

public class Voice {

	public Recognito<String> recognito = new Recognito<>(8000.0f);

	private static final Log logger = LogFactory.getLog(Voice.class);	

	protected String pathVoiceRecorded;

	public Voice() {
	}

	public Voice(String name, String path)
	{
		try {			
				recognito.createVoicePrint(name,
						new File(path));

				logger.info(name + " - " + path);
			
		} catch (UnsupportedAudioFileException e) {
			logger.error("Errore", e);
		} catch (IOException e) {
			logger.error("Errore", e);
		}
	}
	
	public Voice(Map<String, String> voicePrint) {
		try {
			for (Map.Entry<String, String> entry : voicePrint.entrySet()) {

				recognito.createVoicePrint(entry.getKey(),
						new File(entry.getValue()));

				logger.info(entry.getKey() + " - " + entry.getValue());
			}
		} catch (UnsupportedAudioFileException e) {
			logger.error("Errore", e);
		} catch (IOException e) {
			logger.error("Errore", e);
		}
	}

	public Voice(String pathVoiceRecorded) {

		this.pathVoiceRecorded = pathVoiceRecorded;

		try {
			File[] files = new File(pathVoiceRecorded).listFiles();
			// If this pathname does not denote a directory, then listFiles()
			// returns null.

			for (File file : files) {
				if (file.isFile()) {
					recognito.createVoicePrint(file.getName(), new File(
							pathVoiceRecorded + file.getName()));
					logger.info(file.getName() + " - "
							+ pathVoiceRecorded + file.getName());
				}
			}
		} catch (UnsupportedAudioFileException e) {
			logger.error("Errore", e);
		} catch (IOException e) {
			logger.error("Errore", e);
		}
	}

	public String getOptionUser() {

		File[] files = new File(pathVoiceRecorded).listFiles();
		// If this pathname does not denote a directory, then listFiles()
		// returns null.

		StringBuilder sb = new StringBuilder();

		for (File file : files) {
			if (file.isFile()) {
				if (!file.getName().startsWith("req_")) {
					sb.append("<option value=\"" + file.getName() + "\">"
							+ file.getName() + "</option>");
					logger.info(file.getName());
				}
			}
		}
		return sb.toString();
	}
}
