package M03.GUI;

import java.awt.Image;

public class EdificioColocado {
    private Image imagen;
    private int x, y;
    private int ancho, alto;
    private String tipo;
    
    public EdificioColocado(Image imagen, int x, int y, int ancho, int alto, String tipo) {
        this.imagen = imagen;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = tipo;
    }

	public Image getImagen() {
		return imagen;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public String getTipo() {
		return tipo;
	}
}
