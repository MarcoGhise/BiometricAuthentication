package com.bitsinharmony.recognito.webapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bitsinharmony.recognito.MatchResult;

@Controller
public class RecognitoController {

	private Voice voice;

	private static final Log logger = LogFactory
			.getLog(RecognitoController.class);

	@RequestMapping(method = RequestMethod.POST, value = "/uploadnewuser")
	public ModelAndView uploadNewUserWave(
			@RequestParam("file") MultipartFile file,
			@RequestParam("username") String username,
			HttpServletRequest request) throws IOException,
			UnsupportedAudioFileException {
		try {
			byte[] valueDecoded = file.getBytes();

			String filePath = request.getServletContext().getRealPath(
					"/audio/" + username + ".wav");

			logger.info("Upload User:" + username + " File:" + filePath);

			File sample = new File(filePath);

			FileOutputStream os = new FileOutputStream(sample);
			os.write(valueDecoded);
			os.close();

			return new ModelAndView("viewJson", "processed", username + " - OK");
		} catch (Exception e) {
			logger.error("Errore", e);
			return new ModelAndView("viewJson", "processed", "Error!"
					+ e.getMessage());
		}

	}

	@Deprecated
	@RequestMapping(method = RequestMethod.POST, value = "/uploadold")
	public ModelAndView uploadWave(@RequestParam("file") MultipartFile file,
			HttpServletRequest request) throws IOException,
			UnsupportedAudioFileException {

		byte[] valueDecoded = file.getBytes();

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String today = dateFormat.format(date); // 2013/10/15 16:16:39
		today = today.replace(":", "_");

		String filePath = request.getServletContext().getRealPath(
				"/audio/req_" + today + ".wav");

		File sample = new File(filePath);

		FileOutputStream os = new FileOutputStream(sample);
		os.write(valueDecoded);
		os.close();

		// Now check if the King is back
		List<MatchResult<String>> matches = voice.recognito.identify(sample);

		logger.info("");
		logger.info("****************");
		logger.info("");

		logger.info("Sample " + filePath);

		StringBuilder sb = new StringBuilder();

		for (MatchResult<String> result : matches) {
			sb.append("Identified: " + result.getKey() + " distance of "
					+ result.getDistance() + " with "
					+ result.getLikelihoodRatio() + "% positive about it...");
			sb.append("<br>");
		}

		return new ModelAndView("viewJson", "processed", sb.toString());

	}

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public ModelAndView uploadWave(@RequestParam("file") MultipartFile file,
			@RequestParam("username") String username,
			HttpServletRequest request) throws IOException,
			UnsupportedAudioFileException {

		byte[] valueDecoded = file.getBytes();

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String today = dateFormat.format(date); // 2013/10/15 16:16:39
		today = today.replace(":", "_");

		String filePath = request.getServletContext().getRealPath(
				"/audio/req_" + today + ".wav");

		File sample = new File(filePath);

		FileOutputStream os = new FileOutputStream(sample);
		os.write(valueDecoded);
		os.close();

		String filePathMatch = request.getServletContext().getRealPath(
				"/audio/" + username);

		Voice voiceMatch = new Voice(username, filePathMatch);

		List<MatchResult<String>> matches = voiceMatch.recognito
				.identify(sample);

		logger.info("");
		logger.info("****************");
		logger.info("Verify User:" + username + " File:" + filePath);
		logger.info("");

		logger.info("Sample " + filePath);

		StringBuilder sb = new StringBuilder();

		MatchResult<String> result = matches.get(0);

		if (result.getDistance() < 0.1)
			sb.append("OK");
		else
			sb.append("KO");

		logger.info("Identified: " + result.getKey() + " distance of "
				+ result.getDistance() + " with " + result.getLikelihoodRatio()
				+ "% positive about it...");

		return new ModelAndView("viewJson", "processed", sb.toString());

	}

	@RequestMapping(method = RequestMethod.GET, value = "/test")
	public ModelAndView test(HttpServletRequest request) throws IOException {
		return new ModelAndView("viewJson", "Thread Id", "test");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getuserlist")
	public ModelAndView getuserlist(HttpServletRequest request)
			throws IOException {
		return new ModelAndView("viewJson", "processed", voice.getOptionUser());
	}

	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}
}
