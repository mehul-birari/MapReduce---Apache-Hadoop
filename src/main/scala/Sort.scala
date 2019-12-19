import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io
import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import java.io.IOException

import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat


object Sort {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val Config = new Configuration
    val job = Job.getInstance(Config, "word count")
    job.setJarByClass(classOf[Sort])
    job.setMapperClass(classOf[Sort.TokenizerMapper])
    job.setCombinerClass(classOf[Sort.SumReducer])
    job.setReducerClass(classOf[Sort.SumReducer])
    job.setOutputKeyClass(classOf[IntWritable])
    job.setOutputValueClass(classOf[Text])
    job.setMapOutputKeyClass(classOf[IntWritable])
    job.setMapOutputValueClass(classOf[Text])
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    System.exit(if (job.waitForCompletion(true)) 0
    else 1)
  }

  class SumReducer() extends Reducer[IntWritable, Text, Text, IntWritable] {

    @throws[IOException]
    @throws[InterruptedException]
    def reduce(key: IntWritable, values: Iterable[Text], context: Reducer[IntWritable, Text, Text, IntWritable]#Context): Unit = {
      values.foreach(value => {
        context.write(value, key)
      })
    }
  }

  object TokenizerMapper {

  }

  class TokenizerMapper() extends Mapper[LongWritable, Text, IntWritable, Text] {

    @throws[IOException]
    @throws[InterruptedException]
    override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, IntWritable, Text]#Context): Unit = {
      val line = value.toString
      val tokens = line.split("\t")
      print(line)
      context.write(new IntWritable(tokens(1).toInt), new Text(tokens(0)))
    }
  }


}

class Sort{

}