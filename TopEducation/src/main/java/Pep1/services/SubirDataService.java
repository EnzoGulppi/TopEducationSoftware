package Pep1.services;

import Pep1.entities.EstudiantesEntity;
import Pep1.entities.SubirDataEntity;
import Pep1.repositories.EstudiantesRepository;
import Pep1.repositories.SubirDataRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class SubirDataService {
    @Autowired
    private SubirDataRepository dataRepository;

    @Autowired
    private EstudiantesRepository estudiantesRepository;

    private final Logger logg = LoggerFactory.getLogger(SubirDataService.class);

    public ArrayList<SubirDataEntity> obtenerData(){
        return (ArrayList<SubirDataEntity>) dataRepository.findAll();
    }

    public String VerificarArchivo(MultipartFile file) {
        // Verificar si el archivo está vacío
        if (file.isEmpty()) {
            return "Archivo vacío";
        }
        try {
            // Crear un lector CSV
            CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
            // Leer las líneas del archivo CSV
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                // Verificar si cada línea tiene tres columnas
                if (nextLine.length != 3) {
                    return "El archivo debe poseer 3 columnas: Rut, puntaje, fecha";
                }
                String rut = nextLine[0];
                String puntaje = nextLine[1];
                String fecha = nextLine[2];
                // Realizar validaciones específicas para cada columna
                if (!esNumeroEnteroPositivo(puntaje)) {
                    return "Puntaje debe ser un número entero positivo";
                }
                if (!esFormatoFechaValido(fecha)) {
                    return "El campo 'fecha' no tiene un formato de fecha válido (dd-MM-yyyy).";
                }
            }
            // Si cumple con todas las validaciones, retornar una cadena vacía (sin errores)
            return "";
        } catch (IOException | CsvValidationException e) {
            return "Error al intentar procesar archivo";
        }
    }

    private boolean esNumeroEnteroPositivo(String str) {
        try {
            int numero = Integer.parseInt(str);
            return numero >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esFormatoFechaValido(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false); // No permitir fechas inválidas como 32 de enero
        try {
            dateFormat.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    @Generated
    public String guardar(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                }
                catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }
    @Generated
    public void leerCsv(String direccion){
        String texto = "";
        BufferedReader bf = null;
        dataRepository.deleteAll();
        try{
            bf = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            int count = 1;
            while((bfRead = bf.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    GuardarPruebaEnBD(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2]);
                    temp = temp + "\n" + bfRead;
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close();
                }catch(IOException e){
                    logg.error("ERROR", e);
                }
            }
        }
    }
    public void guardarData(SubirDataEntity data){
        dataRepository.save(data);
    }

    public void eliminarData(ArrayList<SubirDataEntity> datas){
        dataRepository.deleteAll(datas);
    }

    @Generated
    public void GuardarPruebaEnBD(String Rut_Estudiante, String Puntaje, String Fecha_Realizacion) {
        // Buscar al estudiante por su Rut
        EstudiantesEntity Estudiante = estudiantesRepository.findByRutEstudiante(Rut_Estudiante);

        // Verificar si el estudiante no existe o el puntaje está fuera de rango
        if (Estudiante == null || !esPuntajeValido(Puntaje)) {
            return;
        }

        // Crear una nueva instancia de PruebaEntity
        SubirDataEntity Prueba = new SubirDataEntity();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");

        // Inicializar los campos de la entidad Prueba
        Prueba.setIdEstudiante(Estudiante.getIdEstudiante());
        Prueba.setPuntaje(Integer.parseInt(Puntaje));
        Prueba.setFechaRealizada(LocalDate.parse(Fecha_Realizacion, formatter));
        Prueba.setFechaResultado(LocalDate.now());

        // Guardar la entidad Prueba en la base de datos
        dataRepository.save(Prueba);
    }

    private boolean esPuntajeValido(String puntaje) {
        if (puntaje.isEmpty()) {
            return true; // Puntaje vacío, se establece un valor predeterminado
        }
        try {
            int valorPuntaje = Integer.parseInt(puntaje);
            return valorPuntaje >= 150 && valorPuntaje <= 1000; // Verificar el rango de puntaje válido
        } catch (NumberFormatException e) {
            return false; // No se pudo convertir el puntaje a un número válido
        }
    }
    public ArrayList<SubirDataEntity> ObtenerPruebasPorRutEstudiante(String Rut) {
        /*Busqueda de ID de estudiante*/
        EstudiantesEntity estudiante = estudiantesRepository.findByRutEstudiante(Rut);

        /*Se verifica que el estudiante exista*/
        if(estudiante == null){
            /*Se crea estructura con 1 elemento*/
            ArrayList<SubirDataEntity> listafinal = new ArrayList<SubirDataEntity>();
            SubirDataEntity Prueba = new SubirDataEntity();
            Prueba.setPuntaje(-1);
            listafinal.add(Prueba);

            return listafinal;
        }
        else {
            /*Busqueda de conjunto de pruebas por por id estudiante*/
            return dataRepository.findAllByEstudianteId(estudiante.getIdEstudiante());
        }
    }

    public Integer PromediosPruebasEstudiante(ArrayList<SubirDataEntity> Pruebas){
        /*Variables internas*/
        int i = 0;              //Contador de recorrido.
        Integer Suma = 0;       //Suma de puntajes.

        if (Pruebas.size() > 0) {
            while (i < Pruebas.size()) {
                Suma = Suma + Pruebas.get(i).getPuntaje();
                i++;
            }
            return (Suma / Pruebas.size());
        }
        else{
            return 0;
        }
    }

}
