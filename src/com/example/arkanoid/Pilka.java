package com.example.arkanoid;

import android.util.Log;

public class Pilka extends ObiektGry{
	
	public static int iloscPilek = 3;
	public static boolean pilkaWgrze = false;
	static float predkosc = 0.3f;		//0.1f = 100 pikseli na sekunde
	
	boolean czyOdbicBok = true;		//zmienna potrzebna aby pilka na ugrz�z�a na kraw�dzi gdy w jednej klatce nie zd�zy oddalic sie na odpowiednia odleglosc
	boolean czyOdbicGora = true;
	
	private int kierunekX, kierunekY;
	
	public Pilka(float x, float y, float szerokosc, int id){
		super(x, y, szerokosc, szerokosc, id);
	    kierunekX = 1;
	    kierunekY = 1;	
	}
	
	public void animuj(long deltaTime)		
	{		
		if(pilkaWgrze)
		{		
			setxPrzesuniecie(this.getxPrzesuniecie() + (predkosc * deltaTime * kierunekX));
			setyPrzesuniecie(this.getyPrzesuniecie() + (predkosc * deltaTime * kierunekY));
			if( (getLewyX() <= -MyGLRenderer.polowaSzerEkranu && czyOdbicBok == true) 
				|| (getPrawyX() >= MyGLRenderer.polowaSzerEkranu && czyOdbicBok == true) )
			{
				kierunekX *= (-1);
				if(kierunekX > 1 || kierunekX < -1)
					kierunekX = kierunekX / 2;
				czyOdbicBok = false;				//gdy pilka odbila sie od sciany to nie moze odbic sie drugi raz i zatrzymac na krawedzi
			}
			if( (getGornyY() >= MyGLRenderer.polowaWysEkranu && czyOdbicGora == true ))
			{
				kierunekY *= -1;
				czyOdbicGora = false;
			}
			if(this.getLewyX() > -MyGLRenderer.polowaSzerEkranu && this.getPrawyX() < (MyGLRenderer.polowaSzerEkranu))
				czyOdbicBok = true;
			if(this.getGornyY() < MyGLRenderer.polowaWysEkranu)
				czyOdbicGora = true;
			if(this.getGornyY() <= -MyGLRenderer.polowaWysEkranu){
				pilkaWgrze = false;
				resetujPilke();
				iloscPilek--;
			}
		}	
	}
	
	void resetujPilke(){
		setxPrzesuniecie(0);
		setyPrzesuniecie(0);
		setKierunekX(1);
		setKierunekY(1);
		pilkaWgrze = false;
	}
	
	public void setKierunekX(int d){
		kierunekX = d;
	}
	
	public int getKierunekX(){
		return kierunekX;
	}
	
	public void setKierunekY(int d){
		kierunekY = d;
	}

	public int getKierunekY(){
		return kierunekY;
	}
	
	
	public float getLewyX(){	//pilka pobiera do kolizji x i y zwiazany z przesunieciem w funkcji rysujacej a nie zwiazany z pozycja wierzcholkow z tworzenia obiektu jak w klockach
		return this.getxPrzesuniecie() - (getSzerokosc() * 0.5f);
	}
	
	public float getPrawyX(){
		return this.getxPrzesuniecie() + (getWysokosc() * 0.5f);
	}
	
	public float getGornyY(){					
		return this.getyPrzesuniecie() + (getWysokosc() * 0.5f);
	}
	
	public float getDolnyY(){
		return this.getyPrzesuniecie() - (getWysokosc() * 0.5f);
	}
}
