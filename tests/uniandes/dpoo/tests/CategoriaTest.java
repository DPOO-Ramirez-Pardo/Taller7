package uniandes.dpoo.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

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
	
	@Test
	public void preOrdenPosOrden() {
		List<NodoAlmacen> preOrden = categoriaRaiz.darPreorden();
		List<NodoAlmacen> posOrden = categoriaRaiz.darPosorden();
		assertEquals("°El preOrden y posOrden tienen tamaÒo diferente!",preOrden.size(),posOrden.size());
		Categoria categoria = (Categoria) preOrden.get(2);
		NodoAlmacen hijo = categoria.darNodos().get(0);
		int preindice = getIndice(preOrden, hijo);
		int posindicepadre = getIndice(posOrden, categoria);
		int posindice = getIndice(posOrden, hijo);
		assertTrue("°Su hijo est· antes que el padre en preorden!", preindice > 2);
		assertTrue("°Su hijo est· antes que el padre en preorden!", posindice < posindicepadre);
	}
}
