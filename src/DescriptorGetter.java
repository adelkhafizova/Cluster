/**
 * Created by ubuntu on 08.02.16.
 */
import chemaxon.clustering.ClusteringException;
import chemaxon.clustering.InvalidLicenseKeyException;
import chemaxon.clustering.JarvisPatrick;
import chemaxon.clustering.Ward;
import chemaxon.descriptors.*;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.qsar.IMolecularDescriptor;

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;

public class DescriptorGetter {

    void run() throws MDReaderException, IOException, MDGeneratorException {
        CFParameters params = new CFParameters();
        GenerateMD descript = new GenerateMD(1);
        descript.setInput("cyp_categories.sdf");
        descript.setSDfileInput(true);
        descript.setDescriptor("cyp_descriptors.sdf", "CF", params, "");
        descript.init();
        descript.run();
        descript.close();
    }

    void ward_run() throws IOException, InvalidLicenseKeyException, SQLException, ClusteringException, CDKException {
         Ward ward=new Ward();
         ward.setInput(new File("dragon_descriptors.sdf"));
         //PrintStream stream = new PrintStream(new File("ames_clusters.txt"));
         ward.setOutput(System.out);
         //ward.setOutput("dragon_clusters.txt");
         ward.setDimensions(0);
         ward.setFpSize(1024);
         ward.setClusterCount(6);
         ward.setCentralShown(false);
         ward.setSingletonNegative(false);
         ward.setStatNeeded(true);
         ward.setOnlyStat(false);
         ward.setStatStream(System.out);
         ward.setKelleyStats("kelley_results.txt");
         ward.setMode(Ward.RNN_AND_CLUSTERING);       // execution mode
         ward.setIdGeneration(true);
         ward.run();
         //stream.close();
        File sdfFile = new File("dragon_categories.sdf");
        IteratingMDLReader reader = new IteratingMDLReader(new FileInputStream(sdfFile), DefaultChemObjectBuilder.getInstance());
        File output = new File("dragon_6clusters.sdf");
        SDFWriter writer = new SDFWriter(new FileWriter(output));
        BufferedReader br = new BufferedReader(new FileReader("dragon_clusters.txt"));
        BufferedReader br2 = new BufferedReader(new FileReader("dragon_descriptors.sdf"));
        //Scanner scanner = new Scanner(new File("ames_clusters.txt"));
        while (reader.hasNext()) {
            IMolecule molecule = (IMolecule) reader.next();
            String cur_line = br.readLine();
            String cur_line2 = br2.readLine();
            String[] parts = cur_line.split("\t");
            int i = Integer.parseInt(parts[0]);
            int j = Integer.parseInt(parts[1]);
            String[] parts2 = cur_line2.split("\t");
            for (Integer k = 0; k < 32; ++k) {
                if (k==0) {
                    molecule.setProperty("desc0", parts2[0].split(" ")[1]);
                } else {
                    molecule.setProperty("desc" + k.toString(), parts2[k]);
                }
            }
            molecule.setProperty("Cluster", j);
            writer.write(molecule);
        }
    }

    void jarvis_patrick_run() throws IOException, InvalidLicenseKeyException, SQLException, ClusteringException, CDKException {
        JarvisPatrick jp=new JarvisPatrick();
        jp.setInput(new File("cyp_descriptors.sdf"));
        //jp.setOutput(System.out);
        jp.setOutput("cyp_clusters.txt");
        jp.setDimensions(0);
        jp.setFpSize(1024);
        jp.setStatNeeded(true);
        jp.setOnlyStat(false);
        jp.setStatStream(System.out);
        jp.setIdGeneration(true);
        jp.setThreshold(0.5f);
        jp.setCentralShown(true);
        jp.setMinCommonRatio(0.5f);
        jp.setSingletonNegative(true);
        jp.run();
        File sdfFile = new File("cyp_categories.sdf");
        IteratingMDLReader reader = new IteratingMDLReader(new FileInputStream(sdfFile), DefaultChemObjectBuilder.getInstance());
        File output = new File("cyp_js_clusters.sdf");
        SDFWriter writer = new SDFWriter(new FileWriter(output));
        BufferedReader br = new BufferedReader(new FileReader("cyp_clusters.txt"));
        br.readLine();
        BufferedReader br2 = new BufferedReader(new FileReader("cyp_descriptors.sdf"));
        while (reader.hasNext()) {
            IMolecule molecule = (IMolecule) reader.next();
            String cur_line = br.readLine();
            String cur_line2 = br2.readLine();
            String[] parts = cur_line.split("\t");
            int i = Integer.parseInt(parts[0]);
            int j = Integer.parseInt(parts[1]);
            String[] parts2 = cur_line2.split("\t");
            for (Integer k = 0; k < 32; ++k) {
                if (k==0) {
                    molecule.setProperty("desc0", parts2[0].split(" ")[1]);
                } else {
                    molecule.setProperty("desc" + k.toString(), parts2[k]);
                }
            }
            molecule.setProperty("Cluster", j);
            writer.write(molecule);
        }
    }

    public static void main(String args[]) {
        try {
            DescriptorGetter dg = new DescriptorGetter();
            //dg.run();
            //dg.ward_run();
            dg.jarvis_patrick_run();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
