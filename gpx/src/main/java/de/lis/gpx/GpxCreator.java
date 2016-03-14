package de.lis.gpx;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
/**
 * Created by dletsch on 01.03.16.
 */
public class GpxCreator {

    private List wfs = new ArrayList();


    private void readCsv(String filename) throws IOException {
        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = CSVFormat.newFormat(';').withHeader().parse(in);

        for (CSVRecord record:records) {
            Map wf = new HashMap();
            wf.put("gem", record.get("Gemarkung"));
            wf.put("fl", record.get("Flurkarte"));
            wf.put("fs", record.get("Flurstueck"));
            wf.put("desc", record.get("Bezeichnung"));
            String latlonStr = record.get("latlon");
            if ( latlonStr.isEmpty() ) {
                String utm = record.get("utm32");
                if ( utm.isEmpty() ) continue;
                latlonStr = utm2latlon(utm);
            }
            if ( latlonStr.isEmpty() ) continue;

            List coords = new ArrayList();
            String latlons[] = record.get("latlon").split(", *");
            for ( String latlon:latlons ) {
                Map ll = new HashMap();
                if ( latlon.isEmpty() ) continue;
                String tmp[] = latlon.split(" ");
                ll.put("lon", tmp[0]);
                ll.put("lat", tmp[1]);
                coords.add(ll);
            }
            int size = coords.size();
            if ( size == 0 ) continue;
            Map lastCoords = (Map) coords.get(size-1);
            Map firstCoords = (Map) coords.get(0);
            if ( !(lastCoords.get("lat").equals(firstCoords.get("lat")) && lastCoords.get("lon").equals(firstCoords.get("lon"))) ) {
                coords.add(firstCoords);
            }

            wf.put("coords",coords);
            wfs.add(wf);
        }
    }

    public List createGrundStuecke ( String inputSource ) throws IOException {

        readCsv(inputSource);

        return wfs;
    }

    String utm2latlon(String utm) {
        String latlon="";
        String utms[] = utm.split(", *");
        CoordinateConversion cc = new CoordinateConversion();
        for ( String u:utms ) {
            String tmp = "32 N " + u;
            double ll[] = cc.utm2LatLon(tmp);
            latlon += String.format("%s %s, ", ll[1], ll[0]);
        }
        return latlon;
    }


    public static void main( String[] args )
            throws Exception
    {
        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();
        /*  next, get the Template  */
        String velocityTemplate = "gpx_rte.vm";
        String inputSource = "Grundstuecke.csv";

        if ( args.length > 0 ) {
            velocityTemplate = args[0];
        }
        if ( args.length > 1 ) {
            inputSource = args[1];
        }

        GpxCreator gpxCreator = new GpxCreator();

        Template t = ve.getTemplate( velocityTemplate );
        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("header", "Wald // Gemarkung Leutra/Maua // Kay Schmidt");
        context.put("wfs", gpxCreator.createGrundStuecke(inputSource));
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        /* show the World */
        System.out.println( writer.toString().replace("ä","ae").replace("Ä","Ae").replace("ü","ue").replace("Ü","ü").replace("ö","oe").replace("Ö","Oe") );
    }

}
