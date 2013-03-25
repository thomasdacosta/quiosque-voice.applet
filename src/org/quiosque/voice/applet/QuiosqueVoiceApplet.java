package org.quiosque.voice.applet;

import java.applet.Applet;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.quiosque.voice.api.QuiosqueVoiceApi;

public class QuiosqueVoiceApplet extends Applet {

	private static final long serialVersionUID = 4574410194897869548L;
	private QuiosqueVoiceApi quiosqueVoiceApi;
	private JFrame frame;
	private JLabel lblMessage;
	
	public void init() {
	    try {
	        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
	            public void run() {
	                createGui();
	            }
	        });
	    } catch (Exception e) {
	        System.err.println("createGUI didn't successfully complete");
	    }
	}
	
	public static void main(String[] args) {
		QuiosqueVoiceApplet quiosqueVoiceApplet = new QuiosqueVoiceApplet();
		quiosqueVoiceApplet.createGui();
	}

	public void createGui() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuiosqueVoiceApplet window = new QuiosqueVoiceApplet();
					window.initialize();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public QuiosqueVoiceApplet() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 262, 182);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Reconhecimento de Voz");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 6, 250, 16);
		frame.getContentPane().add(lblNewLabel);

		lblMessage = new JLabel("Clique em INICIAR");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblMessage.setBounds(6, 34, 250, 48);
		frame.getContentPane().add(lblMessage);

		final JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						btnIniciar.setEnabled(false);
						try {
							initVoiceApi();
							lblMessage.setText("Diga algo");
							String result = quiosqueVoiceApi.recognizer();

							if (result.trim().equals("")) {
								lblMessage.setText("Não sei oque você disse.");
							} else {
								lblMessage.setText(result);
							}
						} catch (Exception e) {
							lblMessage.setText("Ocorreu um erro");
						}
						btnIniciar.setEnabled(true);
					}
				};
				thread.start();
			}
		});
		btnIniciar.setBounds(43, 94, 180, 47);
		frame.getContentPane().add(btnIniciar);
	}

	private void initVoiceApi() throws Exception {
		if (quiosqueVoiceApi == null) {
			lblMessage.setText("Iniciando...");
			quiosqueVoiceApi = new QuiosqueVoiceApi();
			quiosqueVoiceApi.initialize();
		}
	}
}
