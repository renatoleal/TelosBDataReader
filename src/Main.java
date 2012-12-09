

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;

import util.HibernateUtil;
import DAO.DadosDAO;
import DAO.SensoresDAO;
import DAO.UsuarioDAO;
import Entities.Dados;
import Entities.Sensores;
import Entities.Usuario;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SessionFactory sf = HibernateUtil.getSessionFactory();
		SensoresDAO sDAO = new SensoresDAO(sf);
		DadosDAO dDAO = new DadosDAO(sf);
		Socket clientSocket;
		
		try {
			clientSocket = new Socket("192.168.0.110", 9002);
			DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			//BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//Inicia comunicação (recebe os 2 primeiros bytes e os envia de volta)
			System.out.println("Iniciando...");
			int[] ackMessage = new int[2];
			ackMessage[0] = inFromServer.read();
			ackMessage[1] = inFromServer.read();
			System.out.println(ackMessage[0]);
			System.out.println(ackMessage[1]);
			outToServer.write(ackMessage[0]);
			outToServer.write(ackMessage[1]);
			
			boolean readOK = true;
			while(readOK) {
				try {
					int size = inFromServer.read(); //Tamanho do pacote que vai receber
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					String buffer[] = new String[size];
					for(int s = 0; s < size; s++)
					{
						buffer[s] = String.format("%02X", inFromServer.readByte());
						System.out.print(buffer[s] + " ");
					}
					
					//ID do sensor
					int idSensor = Integer.valueOf(buffer[12].concat(buffer[13]), 16);
					Sensores s = sDAO.getById(idSensor); 
					if(s == null) {
						s = new Sensores();
						s.setId(idSensor);
						s.setStatus(true);
						sDAO.save(s);
					}
					
					
					//Contador do sensor
					int contSensor = Integer.valueOf(buffer[14].concat(buffer[15]), 16);
					
					//Leitura de luminosidade
					int lumSensor = Integer.valueOf(buffer[16].concat(buffer[17]), 16);
					Dados d = new Dados(idSensor, lumSensor, new Date().getTime());
					dDAO.save(d);
					
					System.out.println("\n"+ System.currentTimeMillis());
					System.out.println("ID: " + idSensor +  ", Luminosidade: " + lumSensor);
					
					//Verificação dos limites do dado recebido
					UsuarioDAO uDAO = new UsuarioDAO();
					List<Usuario> admins = uDAO.listAdmins();
					if (admins != null) {
						String emailsList = "";
						for (Usuario usuario : admins) {
							emailsList += usuario.getEmail() + ",";
						}
						
						if (s.getMinValue() != null
								&& d.getValue() < s.getMinValue()
								&& (d.getTimeTicks() - s.getLastWarning()) > 120000) {
							s.setLastWarning(d.getTimeTicks());
							sDAO.save(s);
							SendEmail.sendWarningMail(emailsList, idSensor, d.getValue(), d.getTimeTicks());
						}
						else if (s.getMaxValue() != null
								&& d.getValue() > s.getMaxValue()
								&& (d.getTimeTicks() - s.getLastWarning()) > 120000) {
							s.setLastWarning(d.getTimeTicks());
							sDAO.save(s);
							SendEmail.sendWarningMail(emailsList, idSensor, d.getValue(), d.getTimeTicks());
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					readOK = false;
					clientSocket.close();
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
