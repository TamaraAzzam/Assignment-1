import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

class entryForLogs{
    private String time;
    private String level;
    private String message;

    public entryForLogs(String time, String level, String message){
        this.time = time;
        this.level = level;
        this.message = message;

    }

    public String getTime(){
        return time;

    }

    public String getLevel(){
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String toString(){
        return "[" + time + "]" + level + " " + message;

    }

}

public class Processor{
    private Queue<entryForLogs> logQueue;
    private Stack<entryForLogs> errorStack;
    private int infoCount, warningCount, errorCount, memWarnCount;

    public Processor(){
        logQueue = new LinkedList<>();
        errorStack = new Stack<>();
        infoCount = 0;
        errorCount = 0;
        warningCount = 0;
        memWarnCount = 0;

    }

    public void readFile(String fileName){
        try{
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] parts = line.split(" ", 4);
                if(parts.length == 4){
                    String time = parts[0] + " " + parts[1];
                    String level = parts[2];
                    String message = parts[3];
                    entryForLogs logEntry = new entryForLogs(time, level, message);
                    logQueue.add(logEntry);
                }
            }
            scanner.close();
        } catch (Exception exception){
            System.out.println("Cannot read: " + exception.getMessage());
        }

    }

    public void logsProcess() {
        while(!logQueue.isEmpty()){
            entryForLogs log = logQueue.poll();

            switch(log.getLevel()) {
                case "INFO":
                infoCount++;
                break;
                case "WARN":
                    warningCount++;
                    if(log.getMessage().contains("Memory")){
                        memWarnCount++;
                    }
                    break;
                case "ERROR":
                    errorCount++;
                    errorStack.push(log);
                    break;
            }
        }

    }
    public void printCounts(){
        System.out.println("INFO count: " + infoCount);
        System.out.println("WARN count: " + warningCount);
        System.out.println("ERROR count: " + errorCount);
    }

    public void printRecErr() {
        System.out.println("Last 100 ERROR entries:");
        for (int i = Math.max(0, errorStack.size() - 100); i < errorStack.size(); i++) {
            System.out.println(errorStack.get(i));
        }
    }

    public void printMemWarn(){
        System.out.println("Memory warnings count: " + memWarnCount);
    }

    public static void main(String[] args){
        Processor processor = new Processor();
        processor.readFile("src/log-data.csv");
        processor.logsProcess();
        processor.printCounts();
        processor.printRecErr();
        processor.printMemWarn();
    }


}
