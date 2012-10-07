/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package buscador;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author proyectosbeta
 */
public class Indexador {
    // Objetos de clase.
    public static final String directorioDatos = "medicalcd";          // Donde se encuentran los datos
    public static final String directorioIndexar = "index";          // Donde se encuentra lo que se indexó.
    
    // Metodo que crea los indices.
    // Se le pasa un valor booleano.
    // - true: hace de nuevo el indexado.
    // - false: hace indexado incremental.
    public void crearIndice(boolean crearIndice){
        System.out.println("Creando indices...");

        try{
            // Analizador en español. Ya contiene los StopWords en español.
            // El mismo analizador se tiene que usar en el indexado y en la busqueda.
            SpanishAnalyzer analizador = new SpanishAnalyzer(Version.LUCENE_36);                
            
            // Almacenar el indexado en un directorio.
            Directory directorioIndex = new SimpleFSDirectory(new File(directorioIndexar));
            IndexWriter iwriter = new IndexWriter(directorioIndex, analizador, crearIndice, MaxFieldLength.UNLIMITED);
            File directorio = new File(directorioDatos);

            File[] archivos = directorio.listFiles();
            
            for (File archivo : archivos) {
                System.out.println(archivo.getPath());
                
                // Se crea un Documento.
                Document doc = new Document();

                doc.add(new Field("path", archivo.getPath(), Field.Store.YES, Field.Index.ANALYZED));

                Reader reader = new FileReader(archivo.getCanonicalPath());

                doc.add(new Field("contents", reader));
                iwriter.addDocument(doc);
            }
            iwriter.optimize();
            
            int cantidadDeDcoumentosIndexados = iwriter.numDocs();
            System.out.println("Se indexó " + cantidadDeDcoumentosIndexados + " documentos.");
            iwriter.close(); 
            System.out.println("Finalizo el indexado..."); 
            System.out.println("\n"); 
        }catch (Exception e) {
             e.printStackTrace();
        }
    } // Fin del metodo publico crearIndice.
} // Fin de la clase Indexador.