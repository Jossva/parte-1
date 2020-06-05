import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.RandomAccessFile;
import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class InterfazInicial extends JFrame {

	private static Scanner scs;
	private List<Entidad> listaEntidades = new ArrayList<>();
	RandomAccessFile fichero = null, entidades = null, atributos = null;
	private void mostrarEntidad(Entidad entidad) {
		System.out.println("Indice: " + entidad.getIndice());
		System.out.println("Nombre: " + entidad.getNombre());
		System.out.println("Cantidad de atributos: " + entidad.getCantidad());
		System.out.println("Atributos:");
		int i = 1;
		for (Atributo atributo : entidad.getAtributos()) {
			System.out.println("\tNo. " + i);
			System.out.println("\tNombre: " + atributo.getNombre());
			System.out.println("\tTipo de dato: " + atributo.getNombreTipoDato());
			if (atributo.isRequiereLongitud()) {
				System.out.println("\tLongitud: " + atributo.getLongitud());
			}
			i++;
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static Scanner sc;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazInicial frame = new InterfazInicial();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	/**
	 * Create the frame.
	 */
	public InterfazInicial() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 278, 187);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(" B     I     E     N     V     E     N     I     D     O  ");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 11, 296, 22);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("AGREGAR NUEVA ENTIDAD");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String datos = JOptionPane.showInputDialog(null, "¡Hola, Bienvenido al programa!"
						+ "Ingrese su selección, por favor"
						+ "   "
						+ " \n1..... Crear nueva entidad "
						+ "\n2..... agregar registros "
						+ "\n3..... modificar registros"
						+ "\n4..... eliminar registros "
						+ "\n0..... Salir");
		
				int opcion;
				switch(opcion = 1) {
				case 1:
					agregarEntidad();
					break;	
				case 2:
					JOptionPane.showInputDialog(null, "Ingrese la entidad a la que desea agregar registros ");
					break;
				case 3:
					JOptionPane.showInputDialog(null, "Ingrese la entidad a modificar ");
					break;
				case 4:
					JOptionPane.showInputDialog(null, "Entidad a eliminar");
					break;
				case 0:
					if (opcion == 0) {
						JOptionPane.showMessageDialog(null, "Gracias por usar nuestra aplicación, que le vaya bien");
					}
					break;
				}
				
			}

			private boolean agregarEntidad() {
				boolean resultado = false;
				try {
					Entidad entidad = new Entidad();
					entidad.setIndice(listaEntidades.size() + 1);
					JOptionPane.showInputDialog("Ingrese el nombre de la entidad");
					String strNombre = "";
					int longitud = 0;
					do {
						strNombre = sc.nextLine();
						longitud = strNombre.length();
						if (longitud < 2 || longitud > 30) {
							System.out.println("La longitud del nombre no es valida [3 - 30]");
						} else {
							if (strNombre.contains(" ")) {
								System.out
										.println("El nombre no puede contener espacios, sustituya por guion bajo (underscore)");
								longitud = 0;
							}
						}
					} while (longitud < 2 || longitud > 30);
					entidad.setNombre(strNombre);
					System.out.println("Atributos de la entidad");
					int bndDetener = 0;
					do {
						Atributo atributo = new Atributo();
						atributo.setIndice(entidad.getIndice());
						longitud = 0;
						System.out.println("Escriba el nombre del atributo no. " + (entidad.getCantidad() + 1));
						do {
							strNombre = sc.nextLine();
							longitud = strNombre.length();
							if (longitud < 2 || longitud > 30) {
								System.out.println("La longitud del nombre no es valida [3 - 30]");
							} else {
								if (strNombre.contains(" ")) {
									System.out.println(
											"El nombre no puede contener espacios, sustituya por guion bajo (underscore)");
									longitud = 0;
								}
							}
						} while (longitud < 2 || longitud > 30);
						atributo.setNombre(strNombre);
					System.out.println("Seleccione el tipo de dato");
					JOptionPane.showInputDialog(TipoDato.INT.getValue() + " .......... " + TipoDato.INT.name());
					JOptionPane.showInputDialog(TipoDato.LONG.getValue() + " .......... " + TipoDato.LONG.name());
					JOptionPane.showInputDialog(TipoDato.STRING.getValue() + " .......... " + TipoDato.STRING.name());
					JOptionPane.showInputDialog(TipoDato.DOUBLE.getValue() + " .......... " + TipoDato.DOUBLE.name());
					JOptionPane.showInputDialog(TipoDato.FLOAT.getValue() + " .......... " + TipoDato.FLOAT.name());
					JOptionPane.showInputDialog(TipoDato.DATE.getValue() + " .......... " + TipoDato.DATE.name());
					JOptionPane.showInputDialog(TipoDato.CHAR.getValue() + " .......... " + TipoDato.CHAR.name());
						atributo.setValorTipoDato(sc.nextInt());
						if (atributo.isRequiereLongitud()) {
							System.out.println("Ingrese la longitud");
							atributo.setLongitud(sc.nextInt());
						} else {
							atributo.setLongitud(0);
						}
						atributo.setNombreTipoDato();
						entidad.setAtributo(atributo);
						System.out.println("Desea agregar otro atributo presione cualquier numero, de lo contrario 0");
						bndDetener = sc.nextInt();
					} while (bndDetener != 0);
					System.out.println("Los datos a registrar son: ");
					mostrarEntidad(entidad);
					System.out.println("Presione 1 para guardar 0 para cancelar");
					longitud = sc.nextInt();
					if (longitud == 1) {
						// primero guardar atributos
						// establecer la posicion inicial donde se va a guardar
						entidad.setPosicion(atributos.length());
						atributos.seek(atributos.length());// calcular la longitud el archivo
						for (Atributo atributo : entidad.getAtributos()) {
							atributos.writeInt(atributo.getIndice());
							atributos.write(atributo.getBytesNombre());
							atributos.writeInt(atributo.getValorTipoDato());
							atributos.writeInt(atributo.getLongitud());
						atributos.write("\n".getBytes()); // cambio de linea para que el siguiente registro se agregue abajo
						}
						// guardar la entidad
						entidades.writeInt(entidad.getIndice());
						entidades.write(entidad.getBytesNombre());
						entidades.writeInt(entidad.getCantidad());
						entidades.writeInt(entidad.getBytes());
						entidades.writeLong(entidad.getPosicion());
						entidades.write("\n".getBytes()); // cambio de linea para que el siguiente registro se agregue abajo
						listaEntidades.add(entidad);
						resultado = true;
					} else {
						System.out.println("No se guardo la entidad debido a que el usuario decidio cancelarlo");
						resultado = false;
					}
					// https://www.experts-exchange.com/questions/22988755/Some-system-pause-equivalent-in-java.html
					System.out.println("Presione una tecla para continuar...");
					System.in.read();
				} catch (Exception e) {
					resultado = false;
					e.printStackTrace();
				}
				return resultado;
				
			}
			
		});
		

			
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.setBounds(10, 51, 197, 22);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("AGREGAR REGISTROS");
		btnNewButton_1.setForeground(Color.BLUE);
		btnNewButton_1.setBounds(10, 98, 174, 23);
		contentPane.add(btnNewButton_1);
	}

}
