package helloworld;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.batch.repeat.ExitStatus;

/**
 *
 * @author tareq.abedrabbo
 */
public class PrintTasklet implements Tasklet{

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
//    
//    public ExitStatus execute() throws Exception {
//        System.out.print(message + "\n");
//        //return ExitStatus.FINISHED;
//        return ExitStatus.COMPLETED;
//    }

    public RepeatStatus execute(StepContribution sc, ChunkContext cc) throws Exception {
                System.out.print(message + "\n");
        //return ExitStatus.FINISHED;
        return RepeatStatus.FINISHED;
    }

}
