package uniandes.dpoo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import uniandes.cupi2.almacen.mundo.Almacen;
import uniandes.cupi2.almacen.mundo.AlmacenException;
import uniandes.cupi2.almacen.mundo.Categoria;
import uniandes.cupi2.almacen.mundo.NodoAlmacen;

public class AlmacenTest {
	private Almacen almacen;
	private static final String DATA_PATH = "./data/datos.txt";
	
	@BeforeEach @Before
	public void setUp() {
		try {
			almacen = new Almacen(new File(DATA_PATH));
		} catch(AlmacenException e) {
			fail("Excepción durante la construcción de Almacén");
		}
	}
	
	@Test
	public void testMetodo1() {
		assertEquals("El método 1 no retornó el valor correcto.", almacen.metodo1(), "Respuesta 1");
	}
	
	@Test
	public void testMetodo2() {
		assertEquals("El método 2 no retornó el valor correcto.", almacen.metodo2(), "Respuesta 2");
	}
	
	@Test
	public void eliminarNodoRaiz() {
		try {
			almacen.eliminarNodo("1");
			fail("El programa no lanzó error al intentar eliminar la raíz.");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsBuscarNodos")
	public void buscarNodos(String id, String nombre) {
		NodoAlmacen nodo = almacen.buscarNodo(id);
		if (nombre == null) {
			if (nodo != null) {
				fail("�El programa devolvio un nodo que no existia!");
			}
		} else {
			assertEquals("�El nombre del nodo que devolvio el programa no correponde al esperado!", nodo.darNombre(), nombre);
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsEliminarNodos")
	public void eliminarNodos(String id, String idPadre) {
		try {
			Categoria categoria = almacen.eliminarNodo(id);
			if (idPadre == null) {
				if (categoria != null) {
					fail("¡Un nodo que no existe no tiene padre!");
				}
			}
			else {
				assertEquals("�El id de la categoria que devolvio el programa no correponde al esperado!", categoria.darIdentificador(), idPadre);
				NodoAlmacen nodo = almacen.buscarNodo(id);
				if (nodo != null) {
					fail("¡El producto no fue eliminado correctamente!");
				}
			}
		} catch (AlmacenException e) {
			fail("¡El programa lanzo un error al intentar eliminar un nodo diferente a la raíz!");
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarNodoExistente")
	public void agregarNodoExistente(String idPadre, String pTipo, String pIdentificador, String pNombre) {
		try {
			almacen.agregarNodo(idPadre,pTipo,pIdentificador,pNombre);
			fail("¡No se lanzó excepción al agregar un nodo con identificador existente!");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarNodoPadreIncorrecto")
	public void agregarNodoPadreIncorrecto(String idPadre, String pTipo, String pIdentificador, String pNombre) {
		try {
			almacen.agregarNodo(idPadre,pTipo,pIdentificador,pNombre);
			fail("¡No se puede agregar un producto a un padre inexistente!");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarNodoCorrecto")
	public void agregarNodoCorrecto(String idPadre, String pTipo, String pIdentificador, String pNombre) {
		try {
			almacen.agregarNodo(idPadre,pTipo,pIdentificador,pNombre);
			NodoAlmacen nodo = almacen.buscarNodo(pIdentificador);
			if (nodo == null) {
				fail("¡No se encontró el nodo agregado!");
			} else {
				assertEquals("¡El nombre del nodo no corresponde al nombre esperado!",nodo.darNombre(), pNombre);
			}
		} catch (AlmacenException e) {
			fail("¡El método lanzó una excepción a una entrada válida!");
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarProductoExistente")
	public void agregarProductoExistente(String pIdMarca, String pCodigo, String pNombre, String pDescripcion, double pPrecio) {
		try {
			almacen.agregarProducto(pIdMarca, pCodigo, pNombre, pDescripcion, pPrecio);
			fail("¡El método no lanzó excepción al introducir un producto existente!");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarProductoMarcaIncorrecta")
	public void agregarProductoMarcaIncorrecta(String pIdMarca, String pCodigo, String pNombre, String pDescripcion, double pPrecio) {
		try {
			almacen.agregarProducto(pIdMarca, pCodigo, pNombre, pDescripcion, pPrecio);
			fail("¡No se pueden agregar productos a marcas inexistentes!");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarProductoCorrecto")
	public void agregarProductoCorrecto(String pIdMarca, String pCodigo, String pNombre, String pDescripcion, double pPrecio) {
		try {
			almacen.agregarProducto(pIdMarca, pCodigo, pNombre, pDescripcion, pPrecio);
			almacen.venderProducto(pCodigo, 1);
		} catch (AlmacenException e) {
			fail("¡El método lanzó una excepción a una entrada válida! - Mensaje: "+e.getMessage());
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsEliminarProducto")
	public void eliminarProducto(String pCodigo) {
		almacen.eliminarProducto(pCodigo);
		try {
			almacen.venderProducto(pCodigo, 1);
			fail("El almacen no eliminó el producto correctamente.");
		} catch(Exception e) {
			
		}
	}
	private static Stream<Arguments> argumentsBuscarNodos(){
		return Stream.of(
				Arguments.of("",""),
				Arguments.of("",""),
				Arguments.of("","")
				);
	}
	private static Stream<Arguments> argumentsEliminarNodos(){
		return Stream.of(
				Arguments.of("",""),
				Arguments.of("",""),
				Arguments.of("","")

				);
	}
	private static Stream<Arguments> argumentsAgregarNodoExistente(){
		return Stream.of(
				Arguments.of("","","",""),
				Arguments.of("","","",""),
				Arguments.of("","","","")
				);
	}
	private static Stream<Arguments> argumentsAgregarNodoPadreIncorrecto(){
		return Stream.of(
				Arguments.of("","","",""),
				Arguments.of("","","",""),
				Arguments.of("","","","")
				);
	}
	private static Stream<Arguments> argumentsAgregarNodoCorrecto(){
		return Stream.of(
				Arguments.of("","","",""),
				Arguments.of("","","",""),
				Arguments.of("","","","")
				);
	}
	private static Stream<Arguments> argumentsAgregarProductoExistente(){
		return Stream.of(
				Arguments.of("1111","31759941","LED 49\" Smart TV Full HD","AtrÈvete a vivir la mejor experiencia de entretenimiento con el LED 49\" Smart TV Full HD | UN49J5200AKXZL de Samsung, un increIble televisor que combina lo mejor de la tecnologÌa para sorprenderte con im·genes envolventes y tonalidades profundas. Por conectividad no tendr·s que preocuparte, ya que sus 2 entradas HDMI y su puerto USB ser·n ideales para que compartas contenido desde tus dispositivos compatibles y rompas con todos los lÌmites",1549000),
				Arguments.of("5678","30999801","MacBook Air 13,3\" Core i5 8GB 128GB","Apple llega para sorprender a los m·s exigentes con el MacBook Air 13,3\" Core i5 8GB 128 GB | MQD32E/A, un moderno computador port·til que seguro superar· todas tus expectativas gracias al poder otorgado por su procesador Core i5 de 2 n˙cleos y su memoria RAM de 8GB, el d˙o perfecto para responder con fluidez a todos tus requerimientos, rompiendo con todos los lÌmites.",2799990),
				Arguments.of("1131A1","7704791121","SillÛn Dryden","Esta pieza combina un tejido con textura de diamante con una suave microfibra aterciopelada, creando un tapizado ˙nico. Su estructura recta y discreta le brindan versatilidad y le permiten ser usada en diferentes espacios del hogar.",2799930)
				);
	}
	private static Stream<Arguments> argumentsAgregarProductoMarcaIncorrecta(){
		return Stream.of(
				Arguments.of("1111","31759941","LED 49\" Smart TV Full HD","AtrÈvete a vivir la mejor experiencia de entretenimiento con el LED 49\" Smart TV Full HD | UN49J5200AKXZL de Samsung, un increIble televisor que combina lo mejor de la tecnologÌa para sorprenderte con im·genes envolventes y tonalidades profundas. Por conectividad no tendr·s que preocuparte, ya que sus 2 entradas HDMI y su puerto USB ser·n ideales para que compartas contenido desde tus dispositivos compatibles y rompas con todos los lÌmites",1549000),
				Arguments.of("1234","31759941","LED 49\" Smart TV Full HD","AtrÈvete a vivir la mejor experiencia de entretenimiento con el LED 49\" Smart TV Full HD | UN49J5200AKXZL de Samsung, un increÌble televisor que combina lo mejor de la tecnologÌa para sorprenderte con im·genes envolventes y tonalidades profundas. Por conectividad no tendr·s que preocuparte, ya que sus 2 entradas HDMI y su puerto USB ser·n ideales para que compartas contenido desde tus dispositivos compatibles y rompas con todos los lÌmites",1549000),
				Arguments.of("5678","30999801","MacBook Air 13,3\" Core i5 8GB 128GB","Apple llega para sorprender a los m·s exigentes con el MacBook Air 13,3\" Core i5 8GB 128 GB | MQD32E/A, un moderno computador port·til que seguro superar· todas tus expectativas gracias al poder otorgado por su procesador Core i5 de 2 n˙cleos y su memoria RAM de 8GB, el d˙o perfecto para responder con fluidez a todos tus requerimientos, rompiendo con todos los lÌmites.",2799990)
				);
	}

	private static Stream<Arguments> argumentsAgregarProductoCorrecto(){
		return Stream.of(
					Arguments.of("1111","31759941","LED 49\" Smart TV Full HD","AtrÈvete a vivir la mejor experiencia de entretenimiento con el LED 49\" Smart TV Full HD | UN49J5200AKXZL de Samsung, un increÌble televisor que combina lo mejor de la tecnologÌa para sorprenderte con im·genes envolventes y tonalidades profundas. Por conectividad no tendr·s que preocuparte, ya que sus 2 entradas HDMI y su puerto USB ser·n ideales para que compartas contenido desde tus dispositivos compatibles y rompas con todos los lÌmites",1549000),
					Arguments.of("1234","31759941","LED 49\" Smart TV Full HD","AtrÈvete a vivir la mejor experiencia de entretenimiento con el LED 49\" Smart TV Full HD | UN49J5200AKXZL de Samsung, un increÌble televisor que combina lo mejor de la tecnologÌa para sorprenderte con im·genes envolventes y tonalidades profundas. Por conectividad no tendr·s que preocuparte, ya que sus 2 entradas HDMI y su puerto USB ser·n ideales para que compartas contenido desde tus dispositivos compatibles y rompas con todos los lÌmites",1549000),
					Arguments.of("1121","30999801","MacBook Air 13,3\" Core i5 8GB 128GB","Apple llega para sorprender a los m·s exigentes con el MacBook Air 13,3\" Core i5 8GB 128 GB | MQD32E/A, un moderno computador port·til que seguro superar· todas tus expectativas gracias al poder otorgado por su procesador Core i5 de 2 n˙cleos y su memoria RAM de 8GB, el d˙o perfecto para responder con fluidez a todos tus requerimientos, rompiendo con todos los lÌmites.",2799990)
				);
	}

	private static Stream<Arguments> argumentsEliminarProducto(){
		return Stream.of(
					Arguments.of("32206871"),
					Arguments.of("8804692551"),
					Arguments.of("1234567")
				);
	
	


	}

	
	
}
