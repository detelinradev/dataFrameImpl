import java.io.File;

/**
 * Class works with some of the functionality available in the Apache POI library that
 * can read and write files in the Microsoft Office format
 *
 * @author 507560sm Sergey Marchenko
 *
 */
public class FileTools
{

    /**
     *  The writeToXLSX method take a DataFrame and write the contents of that DataFrame
     * to the given output file using Apache POI
     *
     * @param df data frame to be written
     * @param file output directory for data to be written
     */
    public static void writeToXLSX(DataFrame<Double> df, File file) {
    }

    /**
     * The readFromXLSX read the contents of the provided xlsx file which is provided as variable
     * in and convert its contents to a DataFrame object
     *
     * @param f source directory for data to be read
     * @return data frame object that holds the data from the file
     */
    public static DataFrame<Double> readFromXLSX(File f) {
            return null;
    }
}