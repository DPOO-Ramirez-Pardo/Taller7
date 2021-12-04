package uniandes.dpoo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import uniandes.cupi2.almacen.mundo.Almacen;
import uniandes.cupi2.almacen.mundo.AlmacenException;
import uniandes.cupi2.almacen.mundo.Categoria;
import uniandes.cupi2.almacen.mundo.NodoAlmacen;

public class AlmacenTest {
	private Almacen almacen;
	private static final String DATA_PATH = "./data/datos.txt";
	
	@Before
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
			almacen.eliminarNodo("Cupi2");
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
				fail("¡El programa devolvió un nodo que no existía!");
			}
		} else {
			assertEquals("¡El nombre del nodo que devolvió el programa no correponde al esperado!", nodo.darNombre(), nombre);
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
				assertEquals("¡El id de la categoría que devolvió el programa no correponde al esperado!", categoria.darIdentificador(), idPadre);
				NodoAlmacen nodo = almacen.buscarNodo(id);
				if (nodo != null) {
					fail("¡El producto no fue eliminado correctamente!");
				}
			}
		} catch (AlmacenException e) {
			fail("¡El programa lanzó un error al intentar eliminar un nodo diferente a la raíz!");
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
			fail("¡El método no lanzó excepción al introducir un padre incorrecto!");
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
			
		}
	}
}
