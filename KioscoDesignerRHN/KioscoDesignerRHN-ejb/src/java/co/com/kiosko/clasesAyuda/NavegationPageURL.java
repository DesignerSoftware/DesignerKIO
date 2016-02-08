package co.com.kiosko.clasesAyuda;

public enum NavegationPageURL {

	OPCIONESKIOSKO("Opciones Kiosko","/Kiosko/opcionesKiosko.xhtml"), 
	CAMBIARCLAVE("Cambiar clave","/Ingreso/cambiarClave.xhtml"), 
	GENERARREPORTE("Generar Reporte","/Kiosko/generarReporte.xhtml");

	private String page;
	private String url;

	private NavegationPageURL(String page, String url) {
		this.page = page;
		this.url = url;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}