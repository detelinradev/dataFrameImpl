import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import java.util.List;

/**
 * The Random tools class works with some of the functionality available in the Apache Commons Math
 * library. In particular the package org.apache.commons.math3.distribution (documentation) provides
 * a number of useful classes to generate a DataFrame that is filled with data sampled from a
 * given distribution
 *
 * The class contain three static methods that construct a RandomTools object with certain
 * distribution types and generate method that implements the distribution what is used to create instance
 * of the Random tools class
 *
 * It holds Real distribution variable what is used to create dependency with different distribution types
 * hold in the RealDistribution interface of Apache Commons Math API
 *
 * @author Detelin Radev
 *
 */
class RandomTools {
    private RealDistribution realDistribution;

    /**
     * This constructor stores Real distribution variable
     *
     * @param realDistribution dependency variable presenting interface in Apache common API
     */
    private RandomTools(RealDistribution realDistribution) {
        this.realDistribution = realDistribution;
    }

    /**
     * Method creates instance of the class with specific implementation
     *
     * @param i value for the lower bond
     * @param i1 value for the upper bond
     * @return instance of the class with uniform real distribution implementation injected
     */
    static RandomTools uniform(double i, double i1) {
        return new RandomTools(new UniformRealDistribution(i,i1));
    }

    /**
     * Method creates instance of the class with specific implementation
     *
     * @param i value for the given mean
     * @param i1 value for the standard deviation
     * @return instance of the class with normal real distribution implementation injected
     */
    static RandomTools gaussian(double i, double i1) {
        return new RandomTools(new NormalDistribution(i,i1));
    }

    /**
     * Method creates instance of the class with specific implementation
     *
     * @param i value for the given mean
     * @return instance of the class with exponential real distribution implementation injected
     */
    static RandomTools exponential(double i) {
        return new RandomTools(new ExponentialDistribution(i));
    }

    /**
     *  The generate method is called on RandomTools instance, it sets the custom
     * seed of that RealDistribution and then fill a DataFrame with rows and columns corresponding
     * to the colnames list with sample data that is acquired from the RealDistribution object.
     *
     * @param i given seed for the random generator
     * @param rows amount of rows to be created
     * @param asList list with the column names
     * @return data frame filled with sample data
     */
    DataFrame<Double> generate(long i, int rows, List<String> asList) {
        double [][] data = new double[rows][asList.size()];
        realDistribution.reseedRandomGenerator(i);
        for (int j = 0; j < rows; j++) {
            for (int k = 0; k < asList.size(); k++) {
                data[j][k] = realDistribution.sample();
            }
        }
        return new DoubleDataFrame(asList,data);
    }
}