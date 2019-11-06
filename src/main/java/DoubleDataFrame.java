import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * A data frame holds a matrix of data, with the difference that column have
 * names rather than indices.
 *
 * It supports a number of useful operations to manipulate the data that can
 * transform and aggregate the data stored in the matrix.
 *
 * It holds list with column names, two dimensional array with the data stored
 * in the data frame and map for transforming the column names in indices
 *
 * @author Detelin Radev
 *
 */
public class DoubleDataFrame implements DataFrame<Double> {
    private List<String> columnNames;
    private double[][] data;
    private Map<String, Integer> map;

    /**
     * This constructor stores the names of the columns and the data of the data frame.
     * By default, the list of column names associated with the data frame is empty
     * and the array that holds the data is empty as well.
     *
     * @param columnNames list that holds the names of the columns of the data frame
     * @param data array that holds the data of the data frame
     */
    DoubleDataFrame(List<String> columnNames, double[][] data) {
        this.columnNames = columnNames;
        this.data = data;
        this.map = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            map.put(columnNames.get(i), i);
        }
    }

    /**
     * Return the dimensions of the data frame
     *
     * @return the number of the rows in the data frame
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Return the dimensions of the data frame
     *
     * @return the number of the column in the data frame
     */
    @Override
    public int getColumnCount() {
        return data[0].length;
    }

    /**
     * Return a List with the names of the columns in
     * the same order as they were provided when the data frame was created
     *
     * @return the names of the columns in the data frame
     */
    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Performs setting value on specific position in the data frame.
     *
     * @param rowIndex the index of the row where value will be set
     * @param colName the index of the column where value will be set
     * @param value holds the value that will be set
     * @throws IllegalArgumentException if a non-existing column name is provided
     * @throws IndexOutOfBoundsException if an invalid row index is provided
     */
    @Override
    public void setValue(int rowIndex, String colName, Double value) throws IndexOutOfBoundsException, IllegalArgumentException {
        data[rowIndex][map.get(colName)] = value;
    }

    /**
     * Performs retrieving value of specific position in the data frame
     *
     * @param rowIndex the index of the row where value will be retrieved
     * @param colName the index of the column where value will be retrieved
     * @return the value of specific position in the data frame
     * @throws IllegalArgumentException if a non-existing column name is provided.
     * @throws IndexOutOfBoundsException if an invalid row index is provided
     *
     */
    @Override
    public Double getValue(int rowIndex, String colName) throws IndexOutOfBoundsException, IllegalArgumentException {
        return data[rowIndex][map.get(colName)];
    }

    /**
     * Retrieve data vector that holds the values of specific row of the data frame
     *
     * @return data vector that holds the values of specific row of the data frame
     * @param rowIndex the index of the row where value will be retrieved
     * @throws IndexOutOfBoundsException if an invalid row index is provided
     *
     */
    @Override
    public DataVector<Double> getRow(int rowIndex) throws IndexOutOfBoundsException {
        return new DoubleDataVector(data[rowIndex], columnNames, rowIndex);
    }

    /**
     * Retrieve data vector that holds the values of specific column of the data frame
     *
     * @return data vector that holds the values of specific column of the data frame
     * @param colName the index of the row where value will be retrieved
     * @throws IllegalArgumentException if the provided column name does not exist
     *
     */
    @Override
    public DataVector<Double> getColumn(String colName) throws IllegalArgumentException {
        List<Double> list = new ArrayList<>();
        List<String> rowNames = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(data[i][map.get(colName)]);
            rowNames.add("row_" + i);
        }
        return new DoubleDataVector(list, rowNames, colName);
    }

    /**
     * Retrieve list of data vectors that holds the values of all rows of the data frame
     *
     * @return list of data vectors that holds the values of all rows of the data frame
     *
     */
    @Override
    public List<DataVector<Double>> getRows() {
        List<DataVector<Double>> list = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            list.add(getRow(i));
        }


        return list;
    }

    /**
     * Retrieve list of data vectors that holds the values of all columns of the data frame
     *
     * @return list of data vectors that holds the values of all columns of the data frame
     *
     */
    @Override
    public List<DataVector<Double>> getColumns() {
        List<DataVector<Double>> list = new ArrayList<>();
        for (String name : columnNames) {
            list.add(getColumn(name));
        }
        return list;
    }

    /**
     *  Construct a bigger DataFrame object, where extra rows
     * are added to the bottom of the matrix and additional columns with the names defined in newCols
     * are added to the right side of the DataFrame. The data currently stored in the smaller DataFrame
     * object is copied to the bigger object
     *
     * @param additionalRows the extra rows are added to the bottom of the matrix
     * @param newCols list with the names of additional columns to be added to the bottom of the matrix
     * @return data frame with expanded size
     * @throws IllegalArgumentException if an invalid row index is provided
     *
     */
    @Override
    public DataFrame<Double> expand(int additionalRows, List<String> newCols) throws IllegalArgumentException {
        List<String> newCol = new ArrayList<>();
        newCol.addAll(columnNames);
        newCol.addAll(newCols);
        double[][] newData = new double[data.length + additionalRows][columnNames.size() + newCols.size()];
        DataFrame<Double> expanded = new DoubleDataFrame(newCol, newData);
        for (int i = 0; i < data.length; i++) {
            for (String name : columnNames) {
                expanded.setValue(i, name, data[i][map.get(name)]);
            }
        }
        return expanded;
    }

    /**
     * It is used to obtain a DataFrame object with fewer columns from bigger data frame object
     *
     * @param retainColumns collection of columns to be extracted from the original data frame
     * @return data frame with the extracted columns
     * @throws NullPointerException if the collection holds value null
     *
     */
    @Override
    public DataFrame<Double> project(Collection<String> retainColumns)
            throws NullPointerException {
        double[][] newData = new double[data.length][retainColumns.size()];
        List<String> newRetainColumns = new ArrayList<>();
        for(String name: columnNames){
            if(retainColumns.contains(name)) {
                newRetainColumns.add(name);
            }
        }
        DataFrame<Double> df = new DoubleDataFrame(newRetainColumns, newData);
        for (int i = 0; i < data.length; i++) {
            for (String name : newRetainColumns) {
                    df.setValue(i, name, data[i][map.get(name)]);
            }
        }
        return df;
    }

    /**
     * It is used to obtain a DataFrame with fewer rows from bigger data frame object,
     * making use of the Predicate object called rowFilter that contains method test
     * that accepts a DataVector as input, and returns a
     * boolean that indicates if the row associated with the DataVector object should be included in the
     * output DataFrame.
     *
     * @param rowFilter predicate of data vector to be tested against rows of the original data frame
     * @return data frame with the extracted rows
     *
     */
    @Override
    public DataFrame<Double> select(Predicate<DataVector<Double>> rowFilter) {
        int rowCount = 0;
        int newRowIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (rowFilter.test(getRow(i))) {
                rowCount++;
            }
        }
        double[][] newData = new double[rowCount][data[0].length];
        DataFrame<Double> newDf = new DoubleDataFrame(columnNames, newData);
        for (int i = 0; i < data.length; i++) {
            if (rowFilter.test(getRow(i))) {
                System.arraycopy(data[i], 0, newData[newRowIndex], 0, data[0].length);
                newRowIndex++;
            }
        }
        return newDf;
    }

    /**
     * Produce a larger DataFrame object with the same number of
     * rows, but one additional column with name columnName. The values stored in this column
     * are computed by the Function object passed as the second argument. This Function has a
     * method apply that accepts a DataVector as input, and produces the value of data stored in the
     * DataFrame (which is Double) in our case. The method apply the Function to each row in
     * the original DataFrame, and store the output under the column that is being constructed
     *
     * @param columnName name of the additional column
     * @param function computing the values in the additional column
     * @return data frame with the additional column
     *
     */
    @Override
    public DataFrame<Double> computeColumn(String columnName, Function<DataVector<Double>, Double> function) {
        List<String> newColumnNames = new ArrayList<>(columnNames);
        double[][] newData = new double[data.length][data[0].length + 1];
        newColumnNames.add(columnName);
        DataFrame<Double> newDf = new DoubleDataFrame(newColumnNames, newData);

        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, newData[i], 0, data[0].length);
        }

        int rowIndex = 0;
        for (DataVector<Double> row : this) {
            newData[rowIndex++][data[0].length] = function.apply(row);
        }

        return newDf;
    }

    /**
     * Produce a DataVector which holds the name argument as the name
     * that is being returned by DataVector.getName(). Furthermore, this DataVector hold aggregated
     * values for every column in the original DataFrame. These aggregate values are
     * computed using the BinaryOperator argument. A BinaryOperator has a method apply
     * that accepts two Double arguments, and return a single Double output. This operator is
     * first applied to the first and second element in the column, then is applied to the output of the
     * first step and the third element, then is applied to the output of the second step and the fourth
     * element, etc
     *
     * @param name name of the produced data vector
     * @param summaryFunction binary operator that holds the function that should be applied over the data that
     * will be summarized in the data vector that is the result
     * @return hold aggregated values for every column in the original DataFrame
     *
     */
    @Override
    public DataVector<Double> summarize(String name, BinaryOperator<Double> summaryFunction) {

        List<Double> newData = new ArrayList<>();
        double number;
        for (int i = 0; i < data[0].length; i++) {
            List<Double> list = new ArrayList<>();
            for (double[] datum : data) {
                list.add(datum[i]);
            }
            number = list.stream().reduce(summaryFunction).orElse(0d);
            newData.add(number);
        }
        return new DoubleDataVector(newData, columnNames, name);
    }
}