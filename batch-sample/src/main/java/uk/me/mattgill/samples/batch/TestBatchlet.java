package uk.me.mattgill.samples.batch;

import static java.util.logging.Level.INFO;
import static javax.batch.runtime.BatchStatus.COMPLETED;

import java.util.logging.Logger;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Dependent
@Named
public class TestBatchlet extends AbstractBatchlet {

	private static final Logger LOGGER = Logger.getLogger(TestBatchlet.class.getName());

	@Inject
	private JobContext ctx;

	@Override
	public String process() throws Exception {
		LOGGER.log(INFO, "Batchlet with execution ID {0} running", ctx.getExecutionId());
		return COMPLETED.toString();
	}

}