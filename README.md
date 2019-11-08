# dataFrameImpl
 I made this project to help a friend with uni assignment.
 In this assignment I created a small library that implements a data frame, a common type
of data structure popular in data science.
 At the heart of the library, there are two interfaces: DataFrame<E> and DataVector<E>. The
DataFrame interface is designed to represent any kind of data frame, agnostic for the type of data
stored in it. The DataVector interface is designed to represent a single vector of data. Within
this interface, no distinction is made between row or column vectors: the DataVector has a certain
length, has a name associated which each entry (which is useful when it represents a row of data in
the data frame) and also has a name by itself (which is useful when it represents a column of data
in the data frame).
 The interfaces are implemented in two classes : DoubleDataFrame and DataVector<Double>
