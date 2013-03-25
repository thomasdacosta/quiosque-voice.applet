package org.quiosque.voice.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class QuiosqueVoiceApi {

	private static Logger logger = Logger.getAnonymousLogger();

	private ConfigurationManager cm;
	private Recognizer recognizer;
	private Microphone microphone;

	public void initialize() throws Exception {
		cm = new ConfigurationManager(QuiosqueVoiceApi.class.getResource("quiosque.config.xml"));

		recognizer = (Recognizer) cm.lookup("recognizer");
		recognizer.allocate();

		microphone = (Microphone) cm.lookup("microphone");
		if (!microphone.startRecording()) {
			recognizer.deallocate();
			throw new Exception("N‹o foi possivel iniciar o microfone.");
		}
	}

	public String recognizer() {
		String resultText = null;
		Result result = recognizer.recognize();
		if (result != null) {
			resultText = result.getBestFinalResultNoFiller();
		} else {
			logger.info("N‹o foi possivel ouvir oque foi dito.\n");
		}
		return resultText;
	}
	
	public static void main(String[] args) {
		QuiosqueVoiceApi quiosqueVoiceApi = new QuiosqueVoiceApi();
		try {
			quiosqueVoiceApi.initialize();
			while (true) {
				logger.info("Diga alguma coisa");
				String result = quiosqueVoiceApi.recognizer();
				logger.info("***** Voce disse:" + result);
				
				if (result.trim().equalsIgnoreCase("exit")) {
					logger.info("Saindo da aplicacao");
					System.exit(0);
				}
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
}
