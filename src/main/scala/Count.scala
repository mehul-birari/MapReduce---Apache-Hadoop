import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io
import org.apache.hadoop.io.{IntWritable, Text}
import java.io.IOException
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat


object Count {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val Config = new Configuration
    val job = Job.getInstance(Config, "word count")
    job.setJarByClass(classOf[MapReduce])
    job.setMapperClass(classOf[MapReduce.TokenizerMapper])
    job.setCombinerClass(classOf[MapReduce.IntSumReducer])
    job.setReducerClass(classOf[MapReduce.IntSumReducer])
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[IntWritable])
    FileInputFormat.addInputPath(job, new Path(args(0)))
    FileOutputFormat.setOutputPath(job, new Path(args(1)))
    System.exit(if (job.waitForCompletion(true)) 0
    else 1)
  }

  class SumReducer() extends Reducer[Text, IntWritable, Text, IntWritable] {
    private val count = new IntWritable

    @throws[IOException]
    @throws[InterruptedException]
    def reduce(key: Text, values: Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
      var sum = 0
      var value = new io.IntWritable
      val ite = values.iterator
      while ( {
        ite.hasNext
      }) {
        value = ite.next
        sum += value.get
      }
      this.count.set(sum)
      context.write(key, this.count)
    }
  }

  object TokenizerMapper {
    private val one = new IntWritable(1)
  }

  class TokenizerMapper() extends Mapper[AnyRef, Text, Text, IntWritable] {

    @throws[IOException]
    @throws[InterruptedException]
    def map(key: Any, value: Text, context: Mapper[AnyRef, Text, Text, IntWritable]#Context): Unit = {
      context.write(value, TokenizerMapper.one)
    }
  }


}

