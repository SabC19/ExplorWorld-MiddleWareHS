package com.explorworld.model;

import java.util.ArrayList;
import java.util.List;

/**
* Clase genérica utilizada como contenedor estándar para respuestas paginadas.
* Facilita el transporte de datos desde la capa de persistencia hacia la capa de vista
* o cliente, manteniendo la información de los elementos y el conteo total.
* 
* @param <T> El tipo de objeto contenido en la página (ej. Artista, Cancion, etc.).
*/
public class Results<T> {

	/** Lista de elementos de tipo T que conforman la página actual. */
	private List<T> page = new ArrayList<>();
	
	/* OFFSET: Índice de inicio de la página actual en el conjunto total de resultados. */
	private int startIndex;
	
	/* total: Número total de elementos disponibles en el conjunto completo de resultados. */
	private long total;
	
	public Results() {}
	
	/**
	 * Constructor que inicializa la página de resultados, el índice de inicio y el total de elementos.
	 * 
	 * @param page Lista de elementos de tipo T que conforman la página actual.
	 * @param startIndex Índice de inicio de la página actual en el conjunto total de resultados.
	 * @param total Número total de elementos disponibles en el conjunto completo de resultados.
	 */
	public Results(List<T> page, int startIndex, long total) {
		this.page = page;
		this.startIndex = startIndex;
		this.total = total;
	}
	
	public List<T> getPage() {
		return page;
	}
	
	public void setPage(List<T> page) {
		this.page = page;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public long getTotal() {
		return total;
	}
	
	public void setTotal(long total) {
		this.total = total;
	}
}