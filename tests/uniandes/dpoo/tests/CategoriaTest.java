package uniandes.dpoo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;
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

public class CategoriaTest {
	private Almacen almacen;
	private Categoria categoriaRaiz;
	private static final String DATA_PATH = "./data/datos.txt";
	
	@BeforeEach @Before
	public void setUp() {
		try {
			almacen = new Almacen(new File(DATA_PATH));
			categoriaRaiz = almacen.darCategoriaRaiz();
		} catch(AlmacenException e) {
			fail("Excepci√≥n durante la construcci√≥n de Almac√©n");
		}
	}
	
	private int getIndice(List<NodoAlmacen> orden, NodoAlmacen nodo) {
		int i = 0;
		for(NodoAlmacen nodo1: orden) {
			if (nodo1.darIdentificador() == nodo.darIdentificador()) return i;
			i++;
		}
		return -1;
	}
	
	@ParameterizedTest
	@MethodSource("argumentsPreOrdenPosOrden")
	public void preOrdenPosOrden(String categoriaIndex, String hijoIndex) {
		List<NodoAlmacen> preOrden = categoriaRaiz.darPreorden();
		List<NodoAlmacen> posOrden = categoriaRaiz.darPosorden();
		assertEquals("°El preOrden y posOrden tienen tamaÒo diferente!",preOrden.size(),posOrden.size());
		Categoria categoria = (Categoria) categoriaRaiz.buscarNodo(categoriaIndex);
		NodoAlmacen hijo = categoriaRaiz.buscarNodo(hijoIndex);
		int preindice = getIndice(preOrden, hijo);
		int preindicepadre = getIndice(preOrden,categoria);
		int posindicepadre = getIndice(posOrden, categoria);
		int posindice = getIndice(posOrden, hijo);
		assertTrue("°Su hijo est· antes que el padre en el preorden!", preindice > preindicepadre);
		assertTrue("°El padre est· antes que el hijo en el posorden!", posindice < posindicepadre);
	}
	
	private static Stream<Arguments> argumentsPreOrdenPosOrden(){
		return Stream.of(
					Arguments.of("113","1131"),
					Arguments.of("11","1123"),
					Arguments.of("1","1131A1")
				);
	}
	
	@ParameterizedTest
	@MethodSource("argumentsDarValorVentas")
	public void darValorVentas(String line) {
		String args[] = line.split(";");
		String idCategoria = (String) args[0];
		double dineroEsperado = Double.parseDouble(args[1]);
		for (int i = 2; i < args.length; i+=2) {
			almacen.venderProducto((String) args[i], Integer.parseInt((String) args[i+1]));
		}
		assertTrue("°El valor de los productos vendidos no corresponde al esperado!",
				((Categoria) almacen.buscarNodo(idCategoria)).darValorVentas() == dineroEsperado);
	}
	
	private static Stream<Arguments> argumentsDarValorVentas(){
		return Stream.of(
					Arguments.of("113;0.0"),
					Arguments.of("112;1999990;32206871;1"),
					Arguments.of("113;0.0;32206871;1"),
					Arguments.of("112;9999950;32206871;4;32206871;1"),
					Arguments.of("112;23498950;32206871;5;30620211;10")
				);
	}
}
