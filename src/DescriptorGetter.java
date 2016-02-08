/**
 * Created by ubuntu on 08.02.16.
 */
import chemaxon.clustering.ClusteringException;
import chemaxon.clustering.InvalidLicenseKeyException;
import chemaxon.clustering.Ward;
import chemaxon.descriptors.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class DescriptorGetter {

    void run() throws MDReaderException, IOException, MDGeneratorException {
        CFParameters params = new CFParameters();
        GenerateMD descript = new GenerateMD(1);
        descript.setInput("ames_levenberg_experimental.sdf");
        descript.setSDfileInput(true);
        descript.setDescriptor("ames_descriptors.sdf", "CF", params, "");
        descript.init();
        descript.run();
        descript.close();
    }

    void ward_run() throws IOException, InvalidLicenseKeyException, SQLException, ClusteringException {
         Ward ward=new Ward();
         ward.setInput(new File("ames_descriptors.sdf"));
         ward.setOutput(System.out);
         ward.setDimensions(0);
         ward.setFpSize(1024);
         ward.setClusterCount(10);
         ward.setCentralShown(false);
         ward.setSingletonNegative(false);
         ward.setStatNeeded(true);
         ward.setOnlyStat(false);
         ward.setStatStream(System.out);
         ward.setKelleyStats("kelley_results.txt");
         ward.setMode(Ward.RNN_AND_CLUSTERING);       // execution mode
         ward.setIdGeneration(true);
         ward.run();
    }

    public static void main(String args[]) {
        try {
            DescriptorGetter dg = new DescriptorGetter();
            //dg.run();
            dg.ward_run();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
