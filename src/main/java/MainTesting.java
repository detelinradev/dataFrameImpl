
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MainTesting
{

	private static DataFrame<Double> testDataFrame()
	{
		List<String> colNames = Arrays.asList("year", "revenue", "costs");
		double[][] data = { { 2015, 70021.35, 25071.12 },
				            { 2016, 67008.12, 108632.80 },
				            { 2017, 10632.83, 37816.8 },
				            { 2018, 85216.33, 31863.73 } };
		return new DoubleDataFrame(colNames, data);
	}

	public static void main(String[] args)
	{
		dataFrameCore();
		dataFrameCoreSpeed();
		dataVectors();
		testRestructure();
		testAnalysis();
		testRandom();
	}

	private static void dataFrameCore()
	{
		List<String> colNames = Arrays.asList("year", "revenue", "costs");
		double[][] data = { { 2015, 70021.35, 25071.12 },
				{ 2016, 67008.12, 108632.80 },
				{ 2017, 10632.83, 37816.8 },
				{ 2018, 85216.33, 31863.73 } };
		DataFrame<Double> df = new DoubleDataFrame(colNames, data);
		System.out.println("Number of rows: " + df.getRowCount());
		System.out.println("Number of columns: " + df.getColumnCount());
		System.out.println(df.getValue(1, "year"));
		df.print();
		df.setValue(2, "revenue", 0d);
		df.print();
	}

	private static void dataFrameCoreSpeed()
	{
		int size = 10000;
		double[][] data = new double[1][size];
		List<String> header = new ArrayList<>(size);
		for (int j = 0; j < size; j++)
		{
			data[0][j] = j;
			header.add("x_" + j);
		}
		DoubleDataFrame df = new DoubleDataFrame(header, data);
		long time = System.currentTimeMillis();
		for (int j = 0; j < size; j++)
		{
			df.getValue(0, header.get(j));
			df.setValue(0, header.get(j), 0d);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("Running time: " + time + "ms");
	}

	private static void dataVectors()
	{
		DataFrame<Double> df = testDataFrame();
		DataVector<Double> row = df.getRow(1);
		System.out.println(row.getName());
		System.out.println(row.getEntryNames());
		System.out.println(row.getValue("costs").equals(df.getValue(1, "costs")));
		System.out.println(row.getValues());
		System.out.println(row.asMap());
		System.out.println();

		DataVector<Double> col = df.getColumn("costs");
		System.out.println(col.getName());
		System.out.println(col.getEntryNames());
		System.out.println(col.getValue("row_1")
				.equals(df.getValue(1, "costs")));
		System.out.println(col.getValues());
		System.out.println(col.asMap());
		System.out.println();

		System.out.println(df.getColumns().size() == df.getColumnCount());
		System.out.println(df.getRows().size() == df.getRowCount());
		for (DataVector<Double> vec : df)
		{
			System.out.println(vec.getName());
		}
	}

	private static void testRestructure()
	{
		DataFrame<Double> df = testDataFrame();
		DataFrame<Double> bigger = df.expand(1, "profit", "loss");
		bigger.print();
		bigger.setValue(1, "costs", 0d);
		System.out.println(!df.getValue(1, "costs").equals(0d));

		DataFrame<Double> smaller1 = df.project("year", "costs");
		smaller1.print();
		smaller1.setValue(1, "costs", 0d);
		System.out.println(!df.getValue(1, "costs").equals(0d));

		DataFrame<Double> smaller2 = df.select(row -> !row.getValue("year").equals(2017d));
		smaller2.print();
		smaller2.setValue(1, "costs", 0d);
		System.out.println(!df.getValue(1, "costs").equals(0d));
	}

	private static void testAnalysis()
	{
		DataFrame<Double> df = testDataFrame();
		Function<DataVector<Double>, Double> profitFunction;
		profitFunction = row -> row.getValue("revenue") - row.getValue("costs");
		DataFrame<Double> df2 = df.computeColumn("profit", profitFunction);
		df2.print();

		BinaryOperator<Double> sumOp = Double::sum;
		DataVector<Double> dv = df2.summarize("sum", sumOp);
		dv.print();
		df2.summarize("max", Math::max).print();
		df2.summarize("min", Math::min).print();
	}

	private static void testRandom()
	{
		int rows = 10;
		RandomTools rt = RandomTools.uniform(10, 20);
		DataFrame<Double> df;
		df = rt.generate(12345, rows, Arrays.asList("uniform1", "uniform2"));
		rt = RandomTools.gaussian(0, 1);
		df = df.concat(rt.generate(54321, rows, Arrays.asList("normal1", "normal2")));
		rt = RandomTools.exponential(5);
		df = df.concat(rt.generate(1337, rows, Collections.singletonList("exponential")));
		df.print();
	}
}
