# Apache Hadoop MapReduce 

A Map/Reduce program for parallel processing of a DBLP dataset containing entries for various publications at many different venues. File size = 2.5GB
  
## How to run the project

  - Make sure your device is sbt enabled.
  - Clone this private repository and open the sbt shell with the path as the local repository's path.
  - Copy the dataset files "dblp.xml" and "dblp.dtd" into the project directory.
  - Type the command "sbt clean compile run" and execute it.
  - You will get a list of options for which file to run.
  - Select "Parse.scala".
  - After you get the output for this, run and select "Count.scala" and pass the arguments for the path to input file (auths_coauths.txt) and path to output directory (Output_count).
  - After you get the output for this, run and select "Sort.scala" and pass the arguments for the path to input file (Output_count/part-r-00000) and path to output directory (Output_sort).
  - Done.

## How to test the project

  - Type the command "sbt clean compile test" and execute it.
  - Done.

#### Note
  - Run the project in IntelliJ if output doesnt display as shown in the images.
  - Follow the exact path arguments for input and output of each program as shown.
 
 
## OUTPUT
The output shows the various results we obtain after the the mapreduce parallel processing. 


### Analysis
- Parse output where we get the names of Computer Science professors corresponding to the publication and gets displayed the number of times as their publications

![Author names](https://raw.githubusercontent.com/mehul-birari/MapReduce---Apache-Hadoop/master/images/photo1.JPG "Author names")



- Count output where each line shows the count of publications corresponding to the authors whether single or multiple

![Author names and count](https://raw.githubusercontent.com/mehul-birari/MapReduce---Apache-Hadoop/master/images/photo2.JPG "Author names and count")



- Sort output by publication count in ascending order

![Sorted by publication count](https://raw.githubusercontent.com/mehul-birari/MapReduce---Apache-Hadoop/master/images/photo3.JPG "Sorted by publication count")

 
 
### Todos

 - Try for more functionalities.
 - Write MORE Tests
  



   