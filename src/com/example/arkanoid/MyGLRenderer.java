package com.example.arkanoid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer{

	private Context context;		//ta zmienna umozliwia korzystanie z zasobow (jak tekstury, dzwiek)
	private FloatBuffer wspolrzedneTekstur; 		//wspolrzedne sa wspolne dla wszystkich tekstur                   		
	private ShortBuffer kolejnoscWierzcholkow;		// kolejnosc rysowania wierzcholkow OBIEKTU	
	private int[] texIDs = new int[4];				//identyfikatory tekstur
	 static int rozdzielczoscX;
	 static int rozdzielczoscY;
	public static int polowaSzerEkranu;
	public static int polowaWysEkranu;	
	
	List<Pilka> zycia;
	Paletka paletka;
	Pilka pilka;	
	ObiektGry tlo;
	long startTime, deltaTime;
	
	boolean koniecZaladowany;
	static boolean zwyciestwo;
	
	public MyGLRenderer(Context context){
		this.context = context;
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		rozdzielczoscX = dm.widthPixels;		//pobieram szerokosc ekranu
		rozdzielczoscY = dm.heightPixels;
		polowaSzerEkranu = rozdzielczoscX / 2;
		polowaWysEkranu = rozdzielczoscY / 2;	

		zycia = new ArrayList<Pilka>(0);
		Klocek.reset();
		
		koniecZaladowany = false;
		zwyciestwo = false;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		GLES10.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);	//ustawiam kolor tla
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);	//wlaczam tablice wierzcholkow
		
		gl.glEnable(GL10.GL_TEXTURE_2D);					//wlaczam teksturowanie
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	//wlaczam tablice wspolrzednych tekstury
	
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);		//wlaczam alfe dla tekstur
		

		
		/**POCZ�TEK �ADOWANIA TEKSTUR**/
		gl.glGenTextures(4, texIDs, 0);					//tworze identyfikatory tekstur
		
		zaladujTeksture(texIDs[0], R.drawable.tlo, gl);
		zaladujTeksture(texIDs[1], R.drawable.paletka, gl);		
		zaladujTeksture(texIDs[2], R.drawable.kula, gl);
		zaladujTeksture(texIDs[3], R.drawable.klocek, gl);
		/**KONIEC �ADOWANIA TEKSTUR**/

		/** WYZNACZAM MAPOWANIE TEKSTUR **/
		float wTekstur[] = {
			    // Pierwszy
			    0.0f, 1.0f,
			    1.0f, 1.0f,
			    1.0f, 0.0f,
			    0.0f, 0.0f,
			};
		
		wspolrzedneTekstur = stworzBuforWierzcholkow(wTekstur);	
		/**KONIEC MAPOWANIA TEKSTUR **/
		
		inicjalizujTlo();
		inicjalizujPaletke();		
		inicjalizujPilki();
		Klocek.inicjalizujKlocki(texIDs[3]);
		inicjalizujKolejnosc();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES10.glViewport(0, 0, width, height);	
		gl.glOrthof(0, rozdzielczoscX, 0, rozdzielczoscY, -1, 1);	//ustawiam 0,0 w lewym dolnym rogu, szerokosc, wysokosc oraz p�aszczyzny odcinania		
	}

	@Override
	public void onDrawFrame(GL10 gl) 
	{
		GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT);			
		
		deltaTime = (SystemClock.uptimeMillis() - startTime) % 1000L;
		startTime = SystemClock.uptimeMillis();
		pilka.animuj(deltaTime);		
		paletka.wykryjKolizje(pilka);		
							
		narysujNieruchomyObiekt(gl, tlo);
		narysujRuchomyObiekt(gl, paletka);										
		narysujRuchomyObiekt(gl, pilka);
			
		Iterator<Klocek> iterator = Klocek.klocki.iterator();
		boolean pilkaOdbita = false;
		while(iterator.hasNext())
		{
			Klocek klocek = iterator.next();
			if( klocek.wykryjKolizje(pilka) && pilkaOdbita == false){
				iterator.remove();
				if(Klocek.klocki.isEmpty())
					zwyciestwo = true;
				pilkaOdbita = true;
			}
			else
				narysujNieruchomyObiekt(gl, klocek);
		}
		
		
		for(int i=0; i<Pilka.iloscPilek; i++){
			narysujNieruchomyObiekt(gl, zycia.get(i));
		}
		
		if(Pilka.iloscPilek < 0 || Klocek.klocki.isEmpty()){
			if(koniecZaladowany == false){
				Klocek.reset();
				
				Intent intent = new Intent(context.getApplicationContext(), KoniecActivity.class);
				intent.putExtra("wygrana", zwyciestwo);
				context.startActivity(intent);
			}
			koniecZaladowany = true;
		}
			
	}
	
	/*************/
	
	private void inicjalizujPaletke()
	{	
		float szerokosc = rozdzielczoscX * Paletka.szerPom;
		float wysokosc = rozdzielczoscY * Paletka.wysPom;
		float x = rozdzielczoscX * 0.5f - (szerokosc / 2);
		float y = Paletka.odstepY;
		paletka = new Paletka(x, y, szerokosc, wysokosc, texIDs[1]);
	}
	
		
	
	private void inicjalizujTlo(){	      
	    tlo = new ObiektGry(0f, 0f, rozdzielczoscX, rozdzielczoscY, texIDs[0]);
	}
	
	private void inicjalizujPilki(){
		float dlugoscBoku = rozdzielczoscX * 0.025f;
		float x = rozdzielczoscX * 0.5f - (dlugoscBoku / 2);
		float y = rozdzielczoscY * 0.5f - (dlugoscBoku / 2);
		pilka = new Pilka(x, y, dlugoscBoku, texIDs[2]);
		
		x = rozdzielczoscX * 0.05f;
		y = rozdzielczoscY * 0.05f;
		
		for(int i=0; i< Pilka.iloscPilek; i++)
			zycia.add(new Pilka(x + ( (dlugoscBoku + 10)* i ), y, dlugoscBoku, texIDs[2]));
	}
	
	private void inicjalizujKolejnosc(){		//ustalam kolejnosc odwolywania sie do tablic wierzcholkow poszczegolnych obiektow
	    short indeksyWierzcholkow[] = {			//prostokat z dwoch trojkatow o nastepujacych wspolrzednych
			// Pierwszy
			0, 1, 3, 1, 2, 3
		};
	 
		ByteBuffer bb = ByteBuffer.allocateDirect(indeksyWierzcholkow.length * 2); 
		bb.order(ByteOrder.nativeOrder());
		kolejnoscWierzcholkow = bb.asShortBuffer();      
		kolejnoscWierzcholkow.put(indeksyWierzcholkow);            
		kolejnoscWierzcholkow.position(0); 
	}
	
	
	private void zaladujTeksture(int idTekstury, int tekstura, GL10 gl)
	{
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), tekstura);	//wczytanie tekstury z katalogu res/drawable-hdpi
		gl.glBindTexture(GL10.GL_TEXTURE_2D, idTekstury);								//wybranie tekstury na podstawie id wygenerowanego przez funkcje gl.genTextures()
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);								
	}
	
	public static FloatBuffer stworzBuforWierzcholkow(float[] wspolrzedne)
	{
	    ByteBuffer bb = ByteBuffer.allocateDirect(wspolrzedne.length * 4); //przydziel pamiec dla wszystkich wspolrzednych o rozmiarze float (4 bajty)
	    bb.order(ByteOrder.nativeOrder()); 			// u�yj naturalnego porz�dku bajt�w
	    FloatBuffer bufor = bb.asFloatBuffer();      // utw�rz z ByteBuffer FloatBuffer
	    bufor.put(wspolrzedne);           			// dodaj wsp�rz�dne do bufora 
	    bufor.position(0);                			// ustaw pozycj� pocz�tkow� (wskaz element o pozycji 0)
	    
	    return bufor; 
	}
	
	private void narysujRuchomyObiekt(GL10 gl, ObiektGry obiektGry){
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);			
		gl.glBindTexture(GL10.GL_TEXTURE_2D, obiektGry.getIdTekstury());
		gl.glPushMatrix();
		gl.glTranslatef(obiektGry.getxPrzesuniecie(), obiektGry.getyPrzesuniecie(), 0.0f);			
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, wspolrzedneTekstur);		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, obiektGry.getWspolrzedne());
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, kolejnoscWierzcholkow);
		gl.glPopMatrix();
	}
	
	private void narysujNieruchomyObiekt(GL10 gl, ObiektGry obiektGry){
		if(obiektGry instanceof Klocek){
			gl.glColor4f(((Klocek) obiektGry).getKolor()[0],
						 ((Klocek)obiektGry).getKolor()[1],
						 ((Klocek)obiektGry).getKolor()[2], 1f);		
		}
		else
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, obiektGry.getIdTekstury());
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, wspolrzedneTekstur);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, obiektGry.getWspolrzedne());
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, kolejnoscWierzcholkow);
	}
}
