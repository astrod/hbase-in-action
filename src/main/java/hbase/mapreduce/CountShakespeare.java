package hbase.mapreduce;

import hbase.twit.TwitsDAO;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * CountShakespeare
 *
 * @author jongUn
 * @since 2017. 06. 04.
 */
@Service
public class CountShakespeare {

	public static class Map
		extends TableMapper<Text, LongWritable> {

		public static enum Counters {ROWS, SHAKESPEAREAN};
		private Random rand;

		/**
		 * Determines if the message pertains to Shakespeare.
		 */
		private boolean containsShakespear(String msg) {
			return rand.nextBoolean();
		}

		@Override
		protected void setup(Mapper.Context context) {
			rand = new Random(System.currentTimeMillis());
		}

		@Override
		protected void map(
			ImmutableBytesWritable rowkey,
			Result result,
			Mapper.Context context) {
			byte[] b = result.getColumnLatest(
				TwitsDAO.TWITS_FAM,
				TwitsDAO.TWIT_COL).getValue();
			if (b == null) return;

			String msg = Bytes.toString(b);
			if (msg.isEmpty()) return;

			context.getCounter(Counters.ROWS).increment(1);
			if (containsShakespear(msg))
				context.getCounter(Counters.SHAKESPEAREAN).increment(1);
		}
	}

	public void count() throws Exception {
		Job job = Job.getInstance(connection.getConfiguration());
		job.setJarByClass(CountShakespeare.class);

		Scan scan = new Scan();
		scan.addColumn(TwitsDAO.TWITS_FAM, TwitsDAO.TWIT_COL);
		TableMapReduceUtil.initTableMapperJob(
			Bytes.toString(TwitsDAO.TABLE_NAME),
			scan,
			Map.class,
			ImmutableBytesWritable.class,
			Result.class,
			job);

		job.setOutputFormatClass(NullOutputFormat.class);
		job.setNumReduceTasks(0);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	@Autowired
	private Connection connection;
}
