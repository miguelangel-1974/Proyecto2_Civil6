package M03.GUI;

import java.awt.Image;

class EdificioColocado {
    private Image imagen;
    private int x, y;
    private int ancho, alto;
    
    public EdificioColocado(Image imagen, int x, int y, int ancho, int alto) {
        this.imagen = imagen;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
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
}
