
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
		JOptionPane.showMessageDialog(null, "Indice: " + entidad.getIndice()
		+ "\n" + ""
		+ "\n" + "Nombre: " + entidad.getNombre()
		+ "\n" +"Cantidad de atributos: " + entidad.getCantidad()
		+ "\n" + "Atributos: "
		+ "\n" );
		int i = 1;
		for (Atributo atributo : entidad.getAtributos()) {
			JOptionPane.showMessageDialog(null, "\nNo. " + i
					+ "\nNombre: " + atributo.getNombre()
					+ "\nTipo de dato: " + atributo.getNombreTipoDato());
			if (atributo.isRequiereLongitud()) {
				JOptionPane.showMessageDialog(null, "\nLongitud: " + atributo.getLongitud());
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
			System.exit(0);
		}
	
	
	private boolean validarD() {
		boolean res = false;
		try {
			entidades = new RandomAccessFile(rutaEntidades, "rw");
			atributos = new RandomAccessFile(rutaAtributos, "rw");
			long longitud = entidades.length();
			if (longitud <= 0) {
				System.out.println("No hay registros");
				res = false; 
			}
			if (longitud >= bytesEntidad) {
				
				entidades.seek(0);
				Entidad e;
				while (longitud >= bytesEntidad) {
					e = new Entidad();
					e.setIndice(entidades.readInt());
					byte[] bNombre = new byte[30]; 
					entidades.read(bNombre);
					e.setBytesNombre(bNombre);
					e.setCantidad(entidades.readInt());
					e.setBytes(entidades.readInt());
					e.setPosicion(entidades.readLong());
					entidades.readByte();
					longitud -= bytesEntidad;
					
					long longitudAtributos = atributos.length();
					if (longitudAtributos <= 0) {
						System.out.println("No hay registros");
						res = false; 
						break;
					}
					atributos.seek(e.getPosicion());
					Atributo a;
					longitudAtributos = e.getCantidad() * bytesAtributo;
					while (longitudAtributos >= bytesAtributo) {
						a = new Atributo();
						a.setIndice(atributos.readInt());
						byte[] bNombreAtributo = new byte[30]; 
						atributos.read(bNombreAtributo);
						a.setBytesNombre(bNombreAtributo);
						a.setValorTipoDato(atributos.readInt());
						a.setLongitud(atributos.readInt());
						a.setNombreTipoDato();
						atributos.readByte();
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
		opcion = Integer.parseInt(JOptionPane.showInputDialog(null, "Bienvenido al programa"
				+ "\n"
				+ "\n1.... Agregar Entidad "
				+ "\n2.... Modificar Entidad "
				+ "\n3.... Listar Entidades "
				+ "\n0.... Salir"));
				
		if(opcion ==1) {
			if (agregarEntidad()) {
				JOptionPane.showMessageDialog(null, "Entidad agregada con exito");
				mostrarAgregarRegistro = true;
			}else {
				if(opcion == 2) {
					modificarEntidad();
				}
			}
		}else {
			if (opcion == 3) {
				if (listaEntidades.size() > 0) {
					int tmpInt = 0;
					tmpInt = Integer.parseInt(JOptionPane.showInputDialog(null, "Desea imprimir los detalles. Si, presione 1. No, presione 0?"));
					
					if (tmpInt == 1) {
						for (Entidad entidad : listaEntidades) {
							mostrarEntidad(entidad);
						}
					} else {
						for (Entidad entidad : listaEntidades) {
							JOptionPane.showMessageDialog(null, "Indice: " + entidad.getIndice()
							+ "\nNombre: " + entidad.getNombre()
						    + "\nCantidad de atributos: " + entidad.getCantidad());
						}
					}
				} else {
					System.out.println("No hay entidades registradas");
				}
			}
		}
		
	}	
			
}	

private void modificarEntidad() {
			try {
				int indice = 0;
				while (indice < 1 || indice > listaEntidades.size()) {
					for (Entidad entidad : listaEntidades) {
						JOptionPane.showMessageDialog(null, entidad.getIndice() + " ...... " + entidad.getNombre());
					}
					System.out.println("Seleccione la entidad que desea modificar");
					indice = sc.nextInt();
				}
				Entidad entidad = null;
				for (Entidad e : listaEntidades) {
					if (indice == e.getIndice()) {
						entidad = e;
						break;
					}
				}
				String nombreFichero = formarNombreFichero(entidad.getNombre());
				fichero = new RandomAccessFile(rutaPrincipal + nombreFichero, "rw");
				long longitudDatos = fichero.length();
				fichero.close();
				if (longitudDatos > 0) {
					System.out.println("No es posible modificar la entidad debido a que ya tiene datos asociados");
				} else {
					// bandera para verificar que el registro fue encontrado
					boolean bndEncontrado = false, bndModificado = false;
					// posicionarse al principio del archivo
					entidades.seek(0);
					long longitud = entidades.length();
					int registros = 0, salir = 0, i;
					Entidad e;
					byte[] tmpBytes;
					while (longitud > totalBytes) {
						e = new Entidad();
						e.setIndice(entidades.readInt());
						tmpBytes = new byte[30];
						entidades.read(tmpBytes);
						e.setBytesNombre(tmpBytes);
						e.setCantidad(entidades.readInt());
						e.setBytes(entidades.readInt());
						e.setPosicion(entidades.readLong());
						if (entidad.getIndice() == e.getIndice()) {
							System.out.println("Si no desea modificar el campo presione enter");
							System.out.println("Ingrese el nombre");
							String tmpStr = "";
							int len = 0;
							long posicion;
							do {
								tmpStr = sc.nextLine();
								len = tmpStr.length();
								if (len == 1 || len > 30) {
									System.out.println("La longitud del nombre no es valida [2 - 30]");
								}
							} while (len == 1 || len > 30);
							if (len > 0) {
								e.setNombre(tmpStr);
								posicion = registros * totalBytes;
								fichero.seek(posicion);
								fichero.skipBytes(4); // moverse despues del indice (int = 4 bytes)
								// grabar el cambio
								fichero.write(e.getBytesNombre());
								bndModificado = true;
							}
							i = 1;
							for (Atributo a : entidad.getAtributos()) {
								System.out.println("Modificando atributo 1");
								System.out.println(a.getNombre().trim());
							}
							
							break;
						}
						registros++;
						// restar los bytes del registro leido
						longitud -= totalBytes;
					}
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
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
		
			String strNombre;
			strNombre = JOptionPane.showInputDialog(null, "Ingrese el nombre de la entidad");
			
			int longitud = 0;
			do {
				
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
			JOptionPane.showMessageDialog(null, "Atributos de la entidad");
			int bndDetener = 0;
			do {
				Atributo atributo = new Atributo();
				atributo.setIndice(entidad.getIndice());
				longitud = 0;
				JOptionPane.showInputDialog(null, "Escriba el nombre del atributo no. " + (entidad.getCantidad() + 1));
				do {
				
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
				atributo.setNombre(strNombre);
				System.out.println("Seleccione el tipo de dato"
				+ "\n" + ""						
				+ "\n" + TipoDato.INT.getValue() + " .......... " + TipoDato.INT.name()
				+ "\n" + TipoDato.LONG.getValue() + " .......... " + TipoDato.LONG.name()
				+ "\n" + TipoDato.STRING.getValue() + " .......... " + TipoDato.STRING.name()
				+ "\n" + TipoDato.DOUBLE.getValue() + " .......... " + TipoDato.DOUBLE.name()
				+ "\n" + TipoDato.FLOAT.getValue() + " .......... " + TipoDato.FLOAT.name()
				+ "\n" + TipoDato.DATE.getValue() + " .......... " + TipoDato.DATE.name()
				+ "\n" + TipoDato.CHAR.getValue() + " .......... " + TipoDato.CHAR.name());
				atributo.setValorTipoDato(sc.nextInt());
				if (atributo.isRequiereLongitud()) {
					JOptionPane.showInputDialog(null, "Ingrese la longitud");
				
				} else {
					atributo.setLongitud(0);
				}
				atributo.setNombreTipoDato();
				entidad.setAtributo(atributo);
				System.out.println("Desea agregar otro atributo presione cualquier numero, de lo contrario 0");
				bndDetener = sc.nextInt();
			} while (bndDetener != 0);
			System.out.println("Los datos a registrar son: " );
			mostrarEntidad(entidad);
			System.out.println("Presione 1 para guardar 0 para cancelar");
			longitud = sc.nextInt();
			if (longitud == 1) {
				
				entidad.setPosicion(atributos.length());
				atributos.seek(atributos.length());
				for (Atributo atributo : entidad.getAtributos()) {
					atributos.writeInt(atributo.getIndice());
					atributos.write(atributo.getBytesNombre());
					atributos.writeInt(atributo.getValorTipoDato());
					atributos.writeInt(atributo.getLongitud());
				atributos.write("\n".getBytes()); 
				}
			
				entidades.writeInt(entidad.getIndice());
				entidades.write(entidad.getBytesNombre());
				entidades.writeInt(entidad.getCantidad());
				entidades.writeInt(entidad.getBytes());
				entidades.writeLong(entidad.getPosicion());
				entidades.write("\n".getBytes()); 
				listaEntidades.add(entidad);
				resultado = true;
			} else {
				System.out.println("No se guardo la entidad debido a que el usuario decidio cancelarlo");
				resultado = false;
			}
			
			System.out.println("Presione una tecla para continuar...");
			System.in.read();
		} catch (Exception e) {
			resultado = false;
			e.printStackTrace();
		}
		return resultado;
	}
	
}