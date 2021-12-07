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
			fail("ExcepciÃ³n durante la construcciÃ³n de AlmacÃ©n");
		}
	}
	
	@Test
	public void testMetodo1() {
		assertEquals("El mÃ©todo 1 no retornÃ³ el valor correcto.", almacen.metodo1(), "Respuesta 1");
	}
	
	@Test
	public void testMetodo2() {
		assertEquals("El mÃ©todo 2 no retornÃ³ el valor correcto.", almacen.metodo2(), "Respuesta 2");
	}
	
	@Test
	public void eliminarNodoRaiz() {
		try {
			almacen.eliminarNodo("1");
			fail("El programa no lanzÃ³ error al intentar eliminar la raÃ­z.");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsBuscarNodos")
	public void buscarNodos(String id, String nombre) {
		NodoAlmacen nodo = almacen.buscarNodo(id);
		if (nombre == null) {
			if (nodo != null) {
				fail("¡El programa devolvio un nodo que no existia!");
			}
		} else {
			assertEquals("¡El nombre del nodo que devolvio el programa no correponde al esperado!", nodo.darNombre(), nombre);
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsEliminarNodos")
	public void eliminarNodos(String id, String idPadre) {
		try {
			Categoria categoria = almacen.eliminarNodo(id);
			if (idPadre == null) {
				if (categoria != null) {
					fail("Â¡Un nodo que no existe no tiene padre!");
				}
			}
			else {
				assertEquals("¡El id de la categoria que devolvio el programa no correponde al esperado!", categoria.darIdentificador(), idPadre);
				NodoAlmacen nodo = almacen.buscarNodo(id);
				if (nodo != null) {
					fail("Â¡El producto no fue eliminado correctamente!");
				}
			}
		} catch (AlmacenException e) {
			fail("Â¡El programa lanzo un error al intentar eliminar un nodo diferente a la raÃ­z!");
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarNodoExistente")
	public void agregarNodoExistente(String idPadre, String pTipo, String pIdentificador, String pNombre) {
		try {
			almacen.agregarNodo(idPadre,pTipo,pIdentificador,pNombre);
			fail("Â¡No se lanzÃ³ excepciÃ³n al agregar un nodo con identificador existente!");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarNodoPadreIncorrecto")
	public void agregarNodoPadreIncorrecto(String idPadre, String pTipo, String pIdentificador, String pNombre) {
		try {
			almacen.agregarNodo(idPadre,pTipo,pIdentificador,pNombre);
			fail("Â¡No se puede agregar un producto a un padre inexistente!");
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
				fail("Â¡No se encontrÃ³ el nodo agregado!");
			} else {
				assertEquals("Â¡El nombre del nodo no corresponde al nombre esperado!",nodo.darNombre(), pNombre);
			}
		} catch (AlmacenException e) {
			fail("Â¡El mÃ©todo lanzÃ³ una excepciÃ³n a una entrada vÃ¡lida!");
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarProductoExistente")
	public void agregarProductoExistente(String pIdMarca, String pCodigo, String pNombre, String pDescripcion, double pPrecio) {
		try {
			almacen.agregarProducto(pIdMarca, pCodigo, pNombre, pDescripcion, pPrecio);
			fail("Â¡El mÃ©todo no lanzÃ³ excepciÃ³n al introducir un producto existente!");
		} catch (AlmacenException e) {
			
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsAgregarProductoMarcaIncorrecta")
	public void agregarProductoMarcaIncorrecta(String pIdMarca, String pCodigo, String pNombre, String pDescripcion, double pPrecio) {
		try {
			almacen.agregarProducto(pIdMarca, pCodigo, pNombre, pDescripcion, pPrecio);
			fail("Â¡No se pueden agregar productos a marcas inexistentes!");
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
			fail("Â¡El mÃ©todo lanzÃ³ una excepciÃ³n a una entrada vÃ¡lida! - Mensaje: "+e.getMessage());
		}
	}
	
	@ParameterizedTest
	@MethodSource("argumentsEliminarProducto")
	public void eliminarProducto(String pCodigo) {
		almacen.eliminarProducto(pCodigo);
		try {
			almacen.venderProducto(pCodigo, 1);
			fail("El almacen no eliminÃ³ el producto correctamente.");
		} catch(Exception e) {
			
		}
	}
	private static Stream<Arguments> argumentsBuscarNodos(){
		return Stream.of(
				Arguments.of("1132B1","MICA"),
				Arguments.of("1121","ASUS"),
				Arguments.of("1122","Apple")
				);
	}
	private static Stream<Arguments> argumentsEliminarNodos(){
		return Stream.of(
				Arguments.of("113","1"),
				Arguments.of("1131","113"),
				Arguments.of("112","11")
				);
	}
	private static Stream<Arguments> argumentsAgregarNodoExistente(){
		return Stream.of(
				Arguments.of("11","11111","1111","SAMSUNG"),
				Arguments.of("11","112","1121","ASUS"),
				Arguments.of("113","1131","1131A","Sof·s y sillones")
				);
	}
	private static Stream<Arguments> argumentsAgregarNodoPadreIncorrecto(){
		return Stream.of(
				Arguments.of("123","11111","1111","SAMSUNG"),
				Arguments.of("345","112","1121","ASUS"),
				Arguments.of("567","1131","1131A","Sof·s y sillones")
				);
	}
	private static Stream<Arguments> argumentsAgregarNodoCorrecto(){
		return Stream.of(
				Arguments.of("1132","1132A","8804692551","Poltrona Astro"),
				Arguments.of("1132B","1132BC","34266041","Sof· Miami 2 Puestos"),
				Arguments.of("111","1111","31759941","LED 49\" Smart TV Full HD")
				);
	}
	private static Stream<Arguments> argumentsAgregarProductoExistente(){
		return Stream.of(
				Arguments.of("1111","31759941","LED 49\" Smart TV Full HD","AtrÃvete a vivir la mejor experiencia de entretenimiento con el LED 49\" Smart TV Full HD | UN49J5200AKXZL de Samsung, un increIble televisor que combina lo mejor de la tecnologÃa para sorprenderte con imÂ·genes envolventes y tonalidades profundas. Por conectividad no tendrÂ·s que preocuparte, ya que sus 2 entradas HDMI y su puerto USB serÂ·n ideales para que compartas contenido desde tus dispositivos compatibles y rompas con todos los lÃmites",1549000),
				Arguments.of("5678","30999801","MacBook Air 13,3\" Core i5 8GB 128GB","Apple llega para sorprender a los mÂ·s exigentes con el MacBook Air 13,3\" Core i5 8GB 128 GB | MQD32E/A, un moderno computador portÂ·til que seguro superarÂ· todas tus expectativas gracias al poder otorgado por su procesador Core i5 de 2 nËcleos y su memoria RAM de 8GB, el dËo perfecto para responder con fluidez a todos tus requerimientos, rompiendo con todos los lÃmites.",2799990),
				Arguments.of("1131A1","7704791121","SillÃn Dryden","Esta pieza combina un tejido con textura de diamante con una suave microfibra aterciopelada, creando un tapizado Ënico. Su estructura recta y discreta le brindan versatilidad y le permiten ser usada en diferentes espacios del hogar.",2799930)
				);
	}
	private static Stream<Arguments> argumentsAgregarProductoMarcaIncorrecta(){
		return Stream.of(
				Arguments.of("1131A1","7704791121","SillÛn Dryden","><img width=200\" height=\"150\" src=\"file:./data/imagenes/7704791121.jpg\"></img></div> <p>Esta pieza combina un tejido con textura de diamante con una suave microfibra aterciopelada, creando un tapizado ˙nico. Su estructura recta y discreta le brindan versatilidad y le permiten ser usada en diferentes espacios del hogar.</p>",2799930),
				Arguments.of("1131A2","8806952451","Sof· Tel Emelen 3 Puestos","><img width=200\" height=\"150\" src=\"file:./data/imagenes/8806952451.jpg\"></img></div> <p>Este sof· est· fabrciado con una estructura de madera para dar mayor firmeza y resistencia, en su parte interna cuenta con relleno de espuma para brindarte suavidad y acolchado, cuenta con patas de madera y su tapizado es en tela.</p>",1699900),
				Arguments.of("1132B1","24461191","Juego de Comedor Loft 4 Puestos Mesa Blanca","><img width=200\" height=\"150\" src=\"file:./data/imagenes/24461191.jpg\"></img></div> <p>AtrÈvete a cambiar el estilo cl·sico de tu juego de comedor por el diseÒo moderno del Juego de Comedor Loft 4 Puestos Mesa Blanca de Mica. El tono blanco de su mesa resaltar· los colores de las sillas d·ndole un estilo full color a tu hogar.</p>",589990)
				);
	}

	private static Stream<Arguments> argumentsAgregarProductoCorrecto(){
		return Stream.of(
				Arguments.of("1122","1111111","Macbook Pro","Computador de Apple",1700000),
				Arguments.of("1112","222222","Televisor 2021","Televisor Nuevo de LG",9900000),
				Arguments.of("1121","1234567","Asus Vivobook","Computador utilizado para clases virtuales",7000000)
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
