package com.gussssy.orbitalsimulator;

import java.util.ArrayList;
import java.io.Serializable;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
* Used to save and load.
* ////////////////////////////
* 		WORK IN PROGRESS
* ///////////////////////////
*/
public class OrbitalScene implements Serializable{

	ArrayList<Ob> objects;
	ArrayList<Ob> objectsSave;
	ArrayList<Ob> objectsLoad;
	Ob object;

	String fileName = "Saved";
	//ObjectOutputStream os 

	public OrbitalScene(ArrayList<Ob> objects){
		this.objects = objects;

	}
	

	public void save(){
		try{
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
			os.writeObject(objects);
			os.close();

		}catch(FileNotFoundException e){
			e.printStackTrace();

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void open(){
		try{
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
			objectsLoad = (ArrayList<Ob>) is.readObject();
			is.close();
			for(Ob object : objectsLoad){
				System.out.println(object.name);
			}
		}catch(IOException e){
			//catch code
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}

	}




	
	
}