package com.example.arkanoid;

import java.nio.FloatBuffer;

public class ObiektGry {

	private float szerokosc, wysokosc, xPrzesuniecie, yPrzesuniecie;
	private FloatBuffer wspolrzedne;
	private int idTekstury;

	
	public ObiektGry(float xLewyDolny, float yLewyDolny, float szer, float wys, int id){
		float []wsp = {
			xLewyDolny, yLewyDolny, 0,
			xLewyDolny + szer, yLewyDolny, 0,
			xLewyDolny + szer, yLewyDolny + wys, 0,
			xLewyDolny, yLewyDolny + wys, 0
		};
		
		wspolrzedne = MyGLRenderer.stworzBuforWierzcholkow(wsp);
		
		szerokosc = szer;
		wysokosc = wys;
		
		idTekstury = id;
		xPrzesuniecie = 0; yPrzesuniecie =0;
	}
	

	public FloatBuffer getWspolrzedne(){			//pobranie wspolrzednych obiektu do narysowania
		return wspolrzedne;
	}
	
	public float getSzerokosc(){
		return szerokosc;
	}
	
	public float getWysokosc(){
		return wysokosc;
	}
	
	public int getIdTekstury(){
		return idTekstury;
	}

	public float getxPrzesuniecie() {
		return xPrzesuniecie;
	}

	public void setxPrzesuniecie(float xPrzesuniecie) {
		this.xPrzesuniecie = xPrzesuniecie;
	}

	public float getyPrzesuniecie() {
		return yPrzesuniecie;
	}

	public void setyPrzesuniecie(float yPrzesuniecie) {
		this.yPrzesuniecie = yPrzesuniecie;
	}

	
	
}
