import java.util.*;

/**
 * A data vector holds a row of data or column of data, of existing data frame
 *
 * It supports a number of useful operations to retrieve name and/or values of the
 * row or column it holds
 *
 * It holds list with column names, list with column data, list with row names,
 * one dimensional array with the data stored in the data vector, the value for row/column
 * the vector should holds and boolean variable to differentiate what vector holds -
 * column or row
 *
 * @author Detelin Radev
 *
 */
public class DoubleDataVector implements DataVector<Double> {
    private List<Double> columnData;
    private List<String> names;
    private double[] data;
    private int row;
    private String col;
    private boolean isRow;

    /**
     * This constructor stores the names of the columns and the data of the data vector and
     * the index for the row that vector will holds, it holds a boolean variable isRow that
     * declare what vector holds - row or column
     * By default, the list of column names associated with the data vector is empty
     * and the array that holds the data is empty as well, isRow is true , as this constructor
     * creates data vector that holds row
     *
     * @param names list that holds the names of the columns of the data vector
     * @param data array that holds the data of the data vector
     * @param row the index for the row that data vector will hold
     *
     */
    DoubleDataVector(double[] data, List<String> names, int row) {
        this.data = data;
        this.names = names;
        this.row = row;
        isRow = true;
    }

    /**
     * This constructor stores the names of the columns and the data of the data vector and
     * the name for the column that vector will holds, it holds a boolean variable isRow that
     * declare what vector holds - row or column
     * By default, the list of column names associated with the data vector is empty
     * and the array that holds the data is empty as well, isRow is false , as this constructor
     * creates data vector that holds column
     *
     * @param columnData list that holds the names of the columns of the data vector
     * @param names array that holds the data of the data vector
     * @param col the name for the column that data vector will hold
     *
     */
    DoubleDataVector(List<Double> columnData, List<String> names, String col) {
        this.columnData = columnData;
        this.names = names;
        this.col = col;
        isRow = false;
    }

    /**
     * If a DataVector is obtained for a row, the getName() method return "row_0" if it represents
     * the row with index 0, "row_1" if it represents the row with index 1, etc
     * If a DataVector is obtained for a column, the getName() method return the name of the
     * column
     *
     * @return the name of the row/column that data vector holds
     */
    @Override
    public String getName() {
        if (isRow) {
            return "row_" + row;
        }
        return col;
    }

    /**
     * The getEntryNames() method return a list that contains the names of the column names of the original data
     * frame
     *
     * @return list with the names of the rows/columns that data vector consists
     */
    @Override
    public List<String> getEntryNames() {
        if (isRow) {
            return new ArrayList<>(names);
        }
        return new ArrayList<>(names);
    }

    /**
     * The getValue() method return the value associated with
     * the column name that is passed as an argument
     *
     * @return the value associated with the column name that is passed as an argument
     */
    @Override
    public Double getValue(String entryName) {
        if (isRow) {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < names.size(); i++) {
                map.put(names.get(i), i);
            }
            return data[map.get(entryName)];
        }
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            map.put(names.get(i), i);
        }
        return columnData.get(map.get(entryName));
    }
    /**
     *  The method getValues() return a List of all that are stored in this row
     *  in the original data frame
     *
     * @return a List of all that are stored in this row in the original data frame
     */
    @Override
    public List<Double> getValues() {
        if (isRow) {
            List<Double> list = new ArrayList<>();
            for (double value : data) {
                list.add(value);
            }
            return list;
        }
        return columnData;
    }

    /**
     *   Provide a Map object that contains key-value pairs of the column names where keys are presenting
     *  indexes of the array that holds the data
     *
     * @return a Map object that contains key-value pairs of the column names
     */
    @Override
    public Map<String, Double> asMap() {
        if (isRow) {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < names.size(); i++) {
                map.put(names.get(i), i);
            }
            Map<String, Double> asMap = new HashMap<>();
            for (String name : names) {
                asMap.put(name, data[map.get(name)]);
            }
            return asMap;
        }
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            map.put(names.get(i), i);
        }
        Map<String, Double> asMap = new HashMap<>();
        for (String name : names) {
            asMap.put(name, columnData.get(map.get(name)));
        }
        return asMap;
    }

}
