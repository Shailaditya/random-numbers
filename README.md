# random-numbers

Assumption - All the numbers generated will be between Integer.MAX & Integer.MIN

This applications supports two types of action SORT & GENERATE .

Command format -
mvn spring-boot:run -Dspring-boot.run.arguments=--action="SORT input_fle_path output_file_path"
mvn spring-boot:run -Dspring-boot.run.arguments=--action="GENERATE output_file_path"

Example commands -
mvn spring-boot:run -Dspring-boot.run.arguments=--action="SORT C:\input.txt C:\sorted_output.txt"
mvn spring-boot:run -Dspring-boot.run.arguments=--action="GENERATE C:\output.txt"

How application works?

1 - Generate :

For generate task application spawns 4 producer threads which will generate random number using ThreadLocalRandom class & write to a queue in batch.
There is one consumer thread which will read data from queue and write to disk. Reason for using one consumer thread is avoid contention for locks of output files accross multiple writers
One possible enhancement can be using multiple consumers writing to their excusive temp files which can be merged later on.

2 - Sort :

For sorting application uses forkjoin pool to break large file into small chunks recurcively until file is small enough to be loaded into memory for in memory sorting.
Small sorted files are then merged recursively to generate final sorted file.




