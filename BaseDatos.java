import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class BaseDatos {
	Scanner sc = new Scanner(System.in);
	private List<Entidad> listaEntidades = new ArrayList<>();
	RandomAccessFile fichero = null, entidades = null, atributos = null;
	private final String rutaPrincipal = "C:\\Users\\José\\Documents\\ECLIPSE\\newAlumnos\\src";
	private final String rutaEntidades = "C:\\Users\\José\\Documents\\ECLIPSE\\newAlumnos\\src\\entidades.dat";
	private final String rutaAtributos = "C:\\Users\\José\\Documents\\ECLIPSE\\newAlumnos\\src\\atributos.dat";
	private final int totalBytes = 83, bytesEntidad = 47, bytesAtributo = 43;
	private final static String formatoFecha = "dd/MM/yyyy";
	static DateFormat format = new SimpleDateFormat(formatoFecha);
	
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
	

		public static void main(String[] args) {
			BaseDatos bd = new BaseDatos();
			if (bd.validarD()) {
				bd.menu(true);
			} else {
				bd.menu(false);
			}
			System.exit(0); // finalize application
		}
	
	
	private boolean validarD() {
		boolean res = false;
		try {
			entidades = new RandomAccessFile(rutaEntidades, "rw");
			atributos = new RandomAccessFile(rutaAtributos, "rw");
			long longitud = entidades.length();
			if (longitud <= 0) {
				System.out.println("No hay registros");
				res = false; // finalizar el procedimiento
			}
			if (longitud >= bytesEntidad) {
				// posicionarse al principio del archivo
				entidades.seek(0);
				Entidad e;
				while (longitud >= bytesEntidad) {
					e = new Entidad();
					e.setIndice(entidades.readInt());
					byte[] bNombre = new byte[30]; // leer 30 bytes para el nombre
					entidades.read(bNombre);
					e.setBytesNombre(bNombre);
					e.setCantidad(entidades.readInt());
					e.setBytes(entidades.readInt());
					e.setPosicion(entidades.readLong());
					entidades.readByte();// leer el cambio de linea
					longitud -= bytesEntidad;
					// leer atributos
					long longitudAtributos = atributos.length();
					if (longitudAtributos <= 0) {
						System.out.println("No hay registros");
						res = false; // finalizar el procedimiento
						break;
					}
					atributos.seek(e.getPosicion());
					Atributo a;
					longitudAtributos = e.getCantidad() * bytesAtributo;
					while (longitudAtributos >= bytesAtributo) {
						a = new Atributo();
						a.setIndice(atributos.readInt());
						byte[] bNombreAtributo = new byte[30]; // leer 30 bytes para el nombre
						atributos.read(bNombreAtributo);
						a.setBytesNombre(bNombreAtributo);
						a.setValorTipoDato(atributos.readInt());
						a.setLongitud(atributos.readInt());
						a.setNombreTipoDato();
						atributos.readByte();// leer el cambio de linea
						e.setAtributo(a);
						longitudAtributos -= bytesAtributo;
					}
					listaEntidades.add(e);
				}
				if (listaEntidades.size() > 0) {
					res = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
			
		}


	public void menu(boolean mostrarAgregarRegistro) {				
		mostrarAgregarRegistro = false;
		int opcion = 1;
			while (opcion != 0) {
				JOptionPane.showInputDialog(null, "Bienvenido, Ingrese su selecion: "
						+ ""
						+ "\n1 ........ Agregar entidad "
						+ "\n2 ........ Modificar entidad"
						+ "\n3 ........ Listar entidades"
						+ "\n0 ........ Salir");
				opcion = sc.nextInt();
				switch (opcion) {
				case 0:
					JOptionPane.showMessageDialog(null,"Gracias por usar nuestra aplicacion");
					break;
				case 1:
					if (agregarEntidad()) {
						JOptionPane.showMessageDialog(null, "Entidad agregada con exito");
						mostrarAgregarRegistro = true;
					}
					break;
				case 2:

					break;
				case 3:
					if (listaEntidades.size() > 0) {
						int tmpInt = 0;
						System.out.println("Desea imprimir los detalles. Si, presione 1. No, presione 0?");
						tmpInt = sc.nextInt();
						if (tmpInt == 1) {
							for (Entidad entidad : listaEntidades) {
								mostrarEntidad(entidad);
							}
						} else {
							for (Entidad entidad : listaEntidades) {
								System.out.println("Indice: " + entidad.getIndice());
								System.out.println("Nombre: " + entidad.getNombre());
								System.out.println("Cantidad de atributos: " + entidad.getCantidad());
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "No hay entidades registradas");
					}
					break;
				case 4:
					int indice = 0;
					while (indice < 1 || indice > listaEntidades.size()) {
						for (Entidad entidad : listaEntidades) {
							System.out.println(entidad.getIndice() + " ...... " + entidad.getNombre());
						}
						System.out.println("Seleccione la entidad que desea trabajar");
						indice = sc.nextInt();
					}
					iniciar(indice);
					break;
							
				default:
					JOptionPane.showMessageDialog(null, "Opcion no valida");
					break;
				}
			}	
			
				
}

		private String formarNombreFichero(String nombre) {
			return nombre.trim() + ".dat";
			
		}

		
	private void iniciar(int indice) {
		int opcion = 0;
		String nombreFichero = "";
		try {
			Entidad entidad = null;
			for (Entidad e : listaEntidades) {
				if (indice == e.getIndice()) {
					nombreFichero = formarNombreFichero(e.getNombre());
					entidad = e;
					break;
				}
			}
			fichero = new RandomAccessFile(rutaPrincipal + nombreFichero, "rw");
			System.out.println("Bienvenido (a)");
			Atributo a = entidad.getAtributos().get(0);
			do {
				try {
					System.out.println("Seleccione su opcion");
					System.out.println("1.\t\tAgregar");
					System.out.println("2.\t\tListar");
					System.out.println("3.\t\tBuscar");
					System.out.println("4.\t\tModificar");
					System.out.println("0.\t\tRegresar al menu anterior");
					opcion = sc.nextInt();
					switch (opcion) {
					case 0:
						System.out.println("");
						break;
					case 1:
						
						break;
					case 2:
				break;
					case 3:
						System.out.println("Se hara la busqueda en la primera columna ");
						System.out.println("Ingrese " + a.getNombre().trim() + " a buscar");
						// sc.nextLine();
						// encontrarRegistro(carne);
						break;
					case 4:
						System.out.println("Ingrese el carne a modificar: ");
						// carne = sc.nextInt();
						// sc.nextLine();
						// modificarRegistro(carne);
						break;
					default:
						System.out.println("Opcion no valida");
						break;
					}
				} catch (Exception e) { // capturar cualquier excepcion que ocurra
					System.out.println("Error: " + e.getMessage());
				}
			} while (opcion != 0);
			fichero.close();
		} catch (Exception e) { // capturar cualquier excepcion que ocurra
			System.out.println("Error: " + e.getMessage());
		}
		
	}

	private boolean agregarEntidad() {
		boolean resultado = false;
		try {
			Entidad entidad = new Entidad();
			entidad.setIndice(listaEntidades.size() + 1);
			JOptionPane.showInputDialog(null, "Ingrese el nombre de la entidad");
			String strNombre = "";
			int longitud = 0;
			do {
				strNombre = sc.nextLine();
				longitud = strNombre.length();
				if (longitud < 2 || longitud > 30) {
					JOptionPane.showMessageDialog(null, "La longitud del nombre no es valida [3 - 30]");
				} else {
					if (strNombre.contains(" ")) {
						JOptionPane.showMessageDialog(null, "El nombre no puede contener espacios, sustituya por guion bajo (underscore)");
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
				System.out.println(TipoDato.INT.getValue() + " .......... " + TipoDato.INT.name());
				System.out.println(TipoDato.LONG.getValue() + " .......... " + TipoDato.LONG.name());
				System.out.println(TipoDato.STRING.getValue() + " .......... " + TipoDato.STRING.name());
				System.out.println(TipoDato.DOUBLE.getValue() + " .......... " + TipoDato.DOUBLE.name());
				System.out.println(TipoDato.FLOAT.getValue() + " .......... " + TipoDato.FLOAT.name());
				System.out.println(TipoDato.DATE.getValue() + " .......... " + TipoDato.DATE.name());
				System.out.println(TipoDato.CHAR.getValue() + " .......... " + TipoDato.CHAR.name());
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
	
}